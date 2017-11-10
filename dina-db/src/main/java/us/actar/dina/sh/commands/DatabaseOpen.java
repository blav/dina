package us.actar.dina.sh.commands;

import us.actar.dina.sh.Command;
import us.actar.dina.sh.Context;
import us.actar.dina.sh.extensions.DatabaseExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class DatabaseOpen implements Command {

  @Override
  public boolean run (Context context, Scanner arguments) {
    DatabaseExtension extension = context.getExtension (DatabaseExtension.class);
    if (extension.isOpen ()) {
      context.getErr ().printf ("database %s already open.\n", context);
      return true;
    }

    extension.open (arguments.next ());
    try (Connection connection = extension.getConnection ()) {
      try (PreparedStatement s = connection.prepareStatement (
        "CREATE TABLE IF NOT EXISTS program (" +
          "  snapshot INTEGER," +
          "  id INTEGER, " +
          "  parent INTEGER, " +
          "  ip INTEGER, " +
          "  forks INTEGER, " +
          "  faults INTEGER, " +
          "  position INTEGER, " +
          "  size INTEGER, " +
          "  cycles INTEGER, " +
          "  entropy DOUBLE, " +
          "  code CLOB, " +
          "  hash VARCHAR(16), " +
          "  PRIMARY KEY(snapshot, id)" +
          ")")) {
        s.execute ();
      }

      try (PreparedStatement s = connection.prepareStatement (
        "CREATE SEQUENCE IF NOT EXISTS snapshot_id AS INTEGER")) {
        s.execute ();
      }

      try (PreparedStatement s = connection.prepareStatement (
        "CREATE TABLE IF NOT EXISTS snapshot (" +
          "  id INTEGER," +
          "  timestamp BIGINT," +
          "  PRIMARY KEY(id)" +
          ")")) {
        s.execute ();
      }
    } catch (SQLException e) {
      context.getErr ().printf ("could not init database %s: %s\n", context, e.getMessage ());
    }

    return true;
  }
}
