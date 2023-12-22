package rba.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import rba.entity.Person;
import rba.repository.PersonRepository;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class PersonService {

  private final PersonRepository personRepository;
  private static final Logger logger = LoggerFactory.getLogger(PersonService.class);


  public PersonService(PersonRepository personRepository) {
    this.personRepository = personRepository;
  }

  public Person addPerson(Person person) {

    if (personRepository.findById(person.getOib()).isPresent()) {
      logger.error("Osoba sa OIB-om '{}' postoji u bazi podataka.",person.getOib() );
      throw new IllegalArgumentException("Osoba sa OIB-om " + person.getOib() + " postoji u bazi podataka.");
    }
    return personRepository.save(person);
  }

  public Person getPerson(String oib) {
    logger.info("Dohvaćanje osobe sa OIB-om '{}' iz baze podataka", oib);

    Optional<Person> person = personRepository.findById(oib);
    if (person.isEmpty()) {
      logger.error("Osoba sa OIB-om '{}' ne postoji u bazi podataka.", oib);
      throw new IllegalArgumentException("Osoba sa OIB-om " + oib + " ne postoji u bazi podataka.");
    }
    return person.get();
  }

  public void deletePerson(String oib) {

    markFileAsInactive(oib);
    personRepository.delete(getPerson(oib));
    logger.info("Obrisana osoba sa OIB-om '{}' iz baze podataka", oib);
  }

  public String generateFile(String oib) throws IOException {
    logger.info("Generiranje datoteke za OIB: {}", oib);

    if (checkIfFileExists(oib)) {
      logger.error("Datoteka za OIB {} već postoji, ne može se generirati nova", oib);
      throw new IOException("Datoteka za OIB " + oib + " već postoji.");
    }

    Person person = getPerson(oib);
    String fileContent = person.toString();
    File file = new File(oib + "_" + System.currentTimeMillis() + ".txt");

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
      writer.write(fileContent);
      logger.info("Datoteka za OIB {} je uspješno generirana", oib);
    }
    return file.getName();
  }

  public void markFileAsInactive(String oib) {
    try {
      Path fileToModify = findFileByOib(oib);
      if (fileToModify != null) {
        updateFileStatusToInactive(fileToModify);
        logger.info("Status za OIB {} je uspjesno prebacen u NEAKTIVAN u datoteci ", oib);

      }
    } catch (IOException e) {
      logger.error("Došlo je do greške pri ažuriranju datoteke: {}", e.getMessage());
    }
  }

  private boolean checkIfFileExists(String oib) throws IOException {
    logger.info("Provjera dali datoteka postoji sa OIB-om {}", oib);

    return findFileByOib(oib) != null;
  }

  private Path findFileByOib(String oib) throws IOException {
    Pattern pattern = Pattern.compile("^" + oib + ".*\\.txt$");
    AtomicBoolean fileFound = new AtomicBoolean(false);
    Path[] foundFile = new Path[1];

    Files.walkFileTree(Paths.get("."), new SimpleFileVisitor<Path>() {
      @Override
      public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        if (pattern.matcher(file.getFileName().toString()).matches()) {
          fileFound.set(true);
          foundFile[0] = file;
          return FileVisitResult.TERMINATE;
        }
        return FileVisitResult.CONTINUE;
      }
    });

    return fileFound.get() ? foundFile[0] : null;
  }

  private void updateFileStatusToInactive(Path file) throws IOException {
    List<String> lines = Files.readAllLines(file);
    lines = lines.stream()
        .map(line -> line.startsWith("status=") ? "status=NEAKTIVAN" : line)
        .collect(Collectors.toList());

    Files.write(file, lines);
  }
}
