package nl.andrewlalis.teaching_assistant_assistant.model.people;

import javax.persistence.Entity;

/**
 * Represents a student, or someone enrolled and submitting assignments for a course.
 */
@Entity
public class Student extends Person {



    public Student(String firstName, String lastName, String emailAddress) {
        super(firstName, lastName, emailAddress);
    }

}
