package us.actar.dina;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Utils {

  public static Path getBasePath () {
    return createDirectoryIfNotExists (Paths.get (System.getProperty ("user.home"), ".dina"));
  }

  public static Path getSubPath (String path) {
    return createDirectoryIfNotExists (Paths.get (System.getProperty ("user.home"), ".dina", path));
  }

  private static Path createDirectoryIfNotExists (Path path) {
    if (Files.exists (path) == false)
      path.toFile ().mkdirs ();

    return path;
  }
}
