package nl.andrewlalis.teaching_assistant_assistant.model.repositories;

import nl.andrewlalis.teaching_assistant_assistant.model.people.teams.Team;
import org.springframework.data.repository.CrudRepository;

public interface TeamRepository extends CrudRepository<Team, Long> {
}
