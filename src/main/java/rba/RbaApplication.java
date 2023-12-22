package rba;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import rba.db.DatabaseInitializer;

@SpringBootApplication
public class RbaApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(RbaApplication.class, args);
	}

  @Override
  public void run(String... args) throws Exception {
    DatabaseInitializer dbInitializer = new DatabaseInitializer();
    dbInitializer.initializeDatabase();
  }
}
