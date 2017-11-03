package us.actar.dina.console.commands;

import us.actar.dina.console.Command;
import us.actar.dina.console.Context;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class DatabaseOpen implements Command {

  @Override
  public boolean run (Context context, Scanner arguments) {
    ConnectionPool attribute = context.getAttribute (ConnectionPool.class);
    if (attribute.isOpen ()) {
      context.getErr ().printf ("database %s already open.\n", context);
      return true;
    }

    attribute.open (arguments.next ());
    try (Connection connection = attribute.getConnection ()) {
      ;
      try (PreparedStatement s = connection.prepareStatement (
        "CREATE TABLE IF NOT EXISTS program (" +
          "  snapshot INTEGER," +
          "  id INTEGER, " +
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
