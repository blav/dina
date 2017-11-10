package us.actar.dina.sh.commands;

import us.actar.dina.sh.Command;
import us.actar.dina.sh.Context;
import us.actar.dina.sh.extensions.DatabaseExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class DatabaseClear implements Command {

  @Override
  public boolean run (Context context, Scanner arguments) {
    DatabaseExtension extension = context.getExtension (DatabaseExtension.class);
    if (extension.isOpen () == false) {
      context.getErr ().println ("no open database.");
      return true;
    }

    try (Connection connection = extension.getConnection ()) {
      try (PreparedStatement ps = connection.prepareStatement ("TRUNCATE SCHEMA public AND COMMIT")) {
        ps.execute ();
      }

      try (PreparedStatement ps = connection.prepareStatement ("ALTER SEQUENCE snapshot_id RESTART WITH 0")) {
        ps.execute ();
      }
    } catch (SQLException e) {
      e.printStackTrace ();
    }

    return true;
  }
}
