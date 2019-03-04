package nl.andrewlalis.teaching_assistant_assistant.model.people.groups;

import nl.andrewlalis.teaching_assistant_assistant.model.people.Student;

import javax.persistence.Entity;

/**
 * A group of students.
 */
@Entity
public class StudentGroup extends Group<Student> {

    /**
     * Default constructor for JPA.
     */
    public StudentGroup() {}

}
