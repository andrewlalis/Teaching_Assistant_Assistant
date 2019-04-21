package nl.andrewlalis.teaching_assistant_assistant.model.repositories;

import nl.andrewlalis.teaching_assistant_assistant.model.people.teams.StudentTeam;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface StudentTeamRepository extends CrudRepository<StudentTeam, Long> {
    Optional<StudentTeam> findByCourseCodeAndId(String courseCode, long id);
}
