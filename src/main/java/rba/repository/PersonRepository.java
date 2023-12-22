package rba.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rba.entity.Person;

public interface PersonRepository extends JpaRepository<Person, String> {

}

