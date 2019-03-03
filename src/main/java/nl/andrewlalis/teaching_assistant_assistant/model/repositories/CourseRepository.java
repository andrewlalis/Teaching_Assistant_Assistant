package nl.andrewlalis.teaching_assistant_assistant.model.repositories;

import nl.andrewlalis.teaching_assistant_assistant.model.Course;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * The repository used to interact with various Course objects.
 */
@Repository
public interface CourseRepository extends CrudRepository<Course, Long> {
}
