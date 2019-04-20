package nl.andrewlalis.teaching_assistant_assistant.model.repositories;

import nl.andrewlalis.teaching_assistant_assistant.model.people.TeachingAssistant;
import org.springframework.data.repository.CrudRepository;

public interface TeachingAssistantRepository extends CrudRepository<TeachingAssistant, Long> {
}
