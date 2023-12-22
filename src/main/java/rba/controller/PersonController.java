package rba.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rba.entity.Person;
import rba.service.PersonService;

import java.io.IOException;

@RestController
@RequestMapping("/osoba")
public class PersonController {

  private final PersonService personService;

  // Ubacivanje (injection) osobaService putem konstruktora
  public PersonController(PersonService personService) {
    this.personService = personService;
  }

  @PostMapping
  public ResponseEntity<?> addPerson(@RequestBody Person person) {
    // Logika za dodavanje nove osobe
    personService.addPerson(person);
    return new ResponseEntity<>(person,HttpStatus.CREATED);
  }

  @GetMapping("/{oib}")
  public ResponseEntity<Person> getPerson(@PathVariable String oib) {
    // Logika za dohvaćanje osobe po OIB-u
    Person person = personService.getPerson(oib);
    return ResponseEntity.ok(person); // Placeholder odgovor
  }

  @DeleteMapping("/{oib}")
  public ResponseEntity<?> deletePerson(@PathVariable String oib) {
    // Logika za brisanje osobe
    personService.deletePerson(oib);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping("/{oib}/datoteka")
  public ResponseEntity<?> generateFile(@PathVariable String oib) throws IOException {
    // Logika za generiranje datoteke za osobu
    String fileName = personService.generateFile(oib);
    return new ResponseEntity<>(fileName,HttpStatus.OK);
  }
}
