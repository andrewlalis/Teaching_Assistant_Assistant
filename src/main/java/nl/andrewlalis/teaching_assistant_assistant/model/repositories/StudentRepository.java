package nl.andrewlalis.teaching_assistant_assistant.model.repositories;

import nl.andrewlalis.teaching_assistant_assistant.model.people.Student;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface StudentRepository extends CrudRepository<Student, Long> {

    /**
     * Tries to find a student by its unique student number.
     * @param studentNumber The student number to search for.
     * @return An optional Student object.
     */
    public Optional<Student> findByStudentNumber(int studentNumber);

}
