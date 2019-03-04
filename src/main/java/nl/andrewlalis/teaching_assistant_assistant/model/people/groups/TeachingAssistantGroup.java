package nl.andrewlalis.teaching_assistant_assistant.model.people.groups;

import nl.andrewlalis.teaching_assistant_assistant.model.people.TeachingAssistant;

import javax.persistence.Entity;

/**
 * A group of teaching assistants.
 */
@Entity
public class TeachingAssistantGroup extends Group<TeachingAssistant> {

    /**
     * Default constructor for JPA.
     */
    public TeachingAssistantGroup() {}

}
