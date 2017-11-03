package us.actar.dina.console.commands;

import us.actar.dina.console.Command;
import us.actar.dina.console.Context;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class DatabaseClear implements Command {
  @Override
  public boolean run (Context context, Scanner arguments) {
    ConnectionPool attribute = context.getAttribute (ConnectionPool.class);
    if (attribute.isOpen () == false) {
      context.getErr ().println ("no open database.");
      return true;
    }

    try (Connection connection = attribute.getConnection ()) {
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
