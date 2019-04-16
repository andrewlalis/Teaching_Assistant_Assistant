package nl.andrewlalis.teaching_assistant_assistant.model.repositories;

import nl.andrewlalis.teaching_assistant_assistant.model.Course;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The repository used to interact with various Course objects.
 */
@Repository
public interface CourseRepository extends CrudRepository<Course, Long> {

    /**
     * Finds a course by its unique code.
     * @param code A unique code which identifies a course.
     * @return An optional object that may contain the course identified by the given code.
     */
    Optional<Course> findByCode(String code);
}
