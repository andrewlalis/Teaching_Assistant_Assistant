package nl.andrewlalis.teaching_assistant_assistant.model.people;

import nl.andrewlalis.teaching_assistant_assistant.model.Course;
import nl.andrewlalis.teaching_assistant_assistant.model.assignments.grades.SectionGrade;

import javax.persistence.*;
import java.util.List;

/**
 * Represents a teaching assistant of a course, or in other words, a grader and administrator.
 */
@Entity
public class TeachingAssistant extends Person {

    /**
     * The list of courses which this teaching assistant is a member of.
     */
    @ManyToMany
    @JoinTable(
            name = "course_teaching_assistant",
            joinColumns = @JoinColumn(name = "teaching_assistant_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> courses;

    /**
     * The list of all feedback given by a teaching assistant.
     */
    @OneToMany
    @JoinColumn(name = "teaching_assistant_id")
    private List<SectionGrade> sectionGrades;

    @Column
    private TeachingAssistantRole role;

    /**
     * Default constructor for JPA.
     */
    protected TeachingAssistant() {}

}
