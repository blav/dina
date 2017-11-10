package us.actar.dina.sh.extensions;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import us.actar.dina.Utils;
import us.actar.dina.sh.Context;

import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseExtension implements Context.Extension {

  private ComboPooledDataSource pool;

  private String database;

  private boolean compact;

  public void open (String database) {
    this.database = database;
    this.compact = true;
    if (this.pool != null)
      throw new IllegalStateException ("pool already open");

    this.pool = new ComboPooledDataSource ();
    this.pool.setUser ("SA");
    this.pool.setPassword ("");
    this.pool.setJdbcUrl ("jdbc:hsqldb:file:" +
      Paths.get (Utils.getSubPath ("hsqldb").toString (), database)
        .toAbsolutePath ());
  }

  @Override
  public String toString () {
    return database;
  }

  public boolean isCompact () {
    return compact;
  }

  public void setCompact (boolean compact) {
    this.compact = compact;
  }

  public String getDatabase () {
    return database;
  }

  public Connection getConnection () throws SQLException {
    return this.pool.getConnection ();
  }

  public boolean isOpen () {
    return this.pool != null;
  }

  @Override
  public void close () {
    if (pool != null) {
      pool.close ();
      pool = null;
    }
  }
}
