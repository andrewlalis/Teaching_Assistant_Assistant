package nl.andrewlalis.teaching_assistant_assistant.model.repositories;

import nl.andrewlalis.teaching_assistant_assistant.model.people.teams.StudentTeam;
import org.springframework.data.repository.CrudRepository;

public interface StudentTeamRepository extends CrudRepository<StudentTeam, Long> {
}
