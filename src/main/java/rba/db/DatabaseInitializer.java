package rba.db;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseInitializer {

  public void runDatabaseCreationScript() {
    try {
      ProcessBuilder processBuilder = new ProcessBuilder("src/main/resources/db/create_db.sh");
      processBuilder.inheritIO();
      Process process = processBuilder.start();
      int exitCode = process.waitFor();

      if (exitCode != 0) {
        throw new RuntimeException("Database creation script exited with error code: " + exitCode);
      }
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException("Error running database creation script", e);
    }
  }

  public void initializeDatabase() {
    runDatabaseCreationScript();

    String url = "jdbc:postgresql://localhost:5432/rba";
    String user = "rba";
    String password = "rba";

    try (Connection conn = DriverManager.getConnection(url, user, password);
         Statement stmt = conn.createStatement()) {

      String sql = new String(Files.readAllBytes(Paths.get("src/main/resources/db/init_db_rba_person")));
      stmt.executeUpdate(sql);
      System.out.println("Database initialized successfully.");

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}