package nl.andrewlalis.teaching_assistant_assistant.model.people.teams;

import nl.andrewlalis.teaching_assistant_assistant.model.people.TeachingAssistant;
import nl.andrewlalis.teaching_assistant_assistant.model.people.TeachingAssistantRole;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * A group of teaching assistants.
 */
@Entity
public class TeachingAssistantTeam extends Team<TeachingAssistant> {

    @Column
    private TeachingAssistantRole role;

    /**
     * Default constructor for JPA.
     */
    public TeachingAssistantTeam() {}

    @Override
    public void addMember(TeachingAssistant person) {
        this.getMembers().add(person);
    }
}
