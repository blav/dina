package us.actar.dina.console.commands;

import us.actar.dina.Machine;
import us.actar.dina.Program;
import us.actar.dina.ProgramState;
import us.actar.dina.console.Command;
import us.actar.dina.console.Context;

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
import static us.actar.dina.console.commands.Dump.rawDump;

public class DatabaseSnapshot implements Command {

  @Override
  public boolean run (Context context, Scanner arguments) {
    return context.executePaused (c -> {
      ConnectionPoolExtension extension = context.getExtension (ConnectionPoolExtension.class);
      if (extension.isOpen () == false) {
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
          .stream ()
          .forEach (b -> {
              StringBuilder insert = new StringBuilder ("INSERT INTO program " +
                "(snapshot, id, ip, forks, faults, position, size, cycles, entropy, hash, code) VALUES (");

              insert.append (b.stream ()
                .map (i -> (ProgramState) programs.get (i))
                .map (p -> {
                  String code = rawDump (machine, p, new StringWriter ()).toString ();
                  String hash = format ("%016x", hash (machine, p));
                  return format ("(%d, %d, %d, %d, %d, %d, %d, %d, %s, '%s', '%s')",
                    sid,
                    p.getId (), p.getInstructionPointer (), p.getForks (), p.getFaults (),
                    p.getCell ().getOffset (), p.getCell ().getSize (), p.getCycles (),
                    format.format (entropy (machine, p)), hash, code);
                })
                .collect (joining (", ")));

              insert.append (")");

              try (PreparedStatement p = connection.prepareStatement (insert.toString ())) {
                p.execute ();
              } catch (Exception e) {
                System.out.println (insert.toString ());
                throw new RuntimeException (e);
              }
            }
          );
        ;

        connection.commit ();
        context.getOut ().printf ("snapshot %d was created.\n", sid);
      } catch (Exception e) {
        e.printStackTrace ();
      }

      return true;
    });
  }
}
