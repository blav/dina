package us.actar.dina.sh.commands;

import us.actar.dina.Machine;
import us.actar.dina.Program;
import us.actar.dina.sh.Command;
import us.actar.dina.sh.Context;
import us.actar.dina.sh.extensions.DatabaseExtension;

import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.stream.IntStream;

import static java.lang.String.format;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
import static us.actar.dina.Metrics.entropy;
import static us.actar.dina.Metrics.hash;
import static us.actar.dina.sh.commands.Dump.rawDump;

public class DatabaseSnapshot extends Command {

  public DatabaseSnapshot () {
    super ("Creates a snapshot of the current machine state.");
  }

  @Override
  public boolean run (Context context, Scanner arguments) {
    return context.executePaused (c -> {
      DatabaseExtension extension = context.getExtension (DatabaseExtension.class);
      if (! extension.isOpen ()) {
        context.getErr ().println ("no database open.");
        return true;
      }

      try (Connection connection = extension.getConnection ()) {
        Integer snapshot = null;
        try (PreparedStatement s = connection.prepareStatement ("CALL NEXT VALUE FOR snapshot_id")) {
          try (ResultSet rs = s.executeQuery ()) {
            if (rs.next ())
              snapshot = rs.getInt (1);
          }

          if (snapshot == null)
            throw new RuntimeException ("could not generate snapshot snapshot");
        }


        try (PreparedStatement s = connection.prepareStatement (
          "INSERT INTO snapshot (id, timestamp) VALUES (?, ?)")) {
          s.setInt (1, snapshot);
          s.setLong (2, System.currentTimeMillis ());
          s.execute ();
        }

        Machine machine = context.getLoop ().getMachine ();
        List<Program> programs = new ArrayList<> (machine.getPrograms ());
        NumberFormat format = NumberFormat.getNumberInstance (Locale.US);
        int sid = snapshot;
        IntStream.range (0, programs.size ())
          .boxed ()
          .collect (groupingBy (i -> i / 100))
          .values ()
          .forEach (b -> {
              StringBuilder insert = new StringBuilder ("INSERT INTO program " +
                "(snapshot, id, parent, generation, ip, forks, faults, position, size, cycles, skipped, entropy, energy, hash, code) VALUES (");

              insert.append (b.stream ()
                .map (programs::get)
                .map (p -> {
                  String code = rawDump (machine, p, new StringWriter ()).toString ();
                  String hash = format ("%016x", hash (machine, p));
                  return format ("(%d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %s, %d, '%s', '%s')",
                    sid,
                    p.getId (), p.getParentId (), p.getGeneration (), p.getInstructionPointer (), p.getForks (), p.getFaults (),
                    p.getCell ().getOffset (), p.getCell ().getSize (), p.getCycles (), p.getSkipped (),
                    format.format (entropy (machine, p)), p.getEnergy (), hash, code);
                })
                .collect (joining (", ")));

              insert.append (")");

              try (PreparedStatement p = connection.prepareStatement (insert.toString ())) {
                p.execute ();
              } catch (Exception e) {
                System.out.println (insert);
                throw new RuntimeException (e);
              }
            }
          );

        connection.commit ();
        context.getOut ().printf ("snapshot %d was created.\n", sid);
      } catch (Exception e) {
        e.printStackTrace (context.getErr ());
      }

      return true;
    });
  }
}
