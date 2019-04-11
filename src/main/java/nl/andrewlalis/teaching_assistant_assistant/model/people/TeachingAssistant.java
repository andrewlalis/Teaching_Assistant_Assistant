package nl.andrewlalis.teaching_assistant_assistant.model.people;

import javax.persistence.Entity;

/**
 * Represents a teaching assistant of a course, or in other words, a grader and administrator.
 */
@Entity
public class TeachingAssistant extends Person {

    /**
     * Default constructor for JPA.
     */
    protected TeachingAssistant() {

    }

    public TeachingAssistant(String firstName, String lastName, String emailAddress) {
        super(firstName, lastName, emailAddress);
    }
}
