package nl.andrewlalis.teaching_assistant_assistant.model.repositories;

import nl.andrewlalis.teaching_assistant_assistant.model.people.Person;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person, Long> {

    Person findByFirstNameAndLastName(String firstName, String lastName);

}
