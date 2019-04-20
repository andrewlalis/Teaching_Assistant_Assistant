package nl.andrewlalis.teaching_assistant_assistant.model.repositories;

import nl.andrewlalis.teaching_assistant_assistant.model.people.teams.TeachingAssistantTeam;
import org.springframework.data.repository.CrudRepository;

public interface TeachingAssistantTeamRepository extends CrudRepository<TeachingAssistantTeam, Long> {
}
