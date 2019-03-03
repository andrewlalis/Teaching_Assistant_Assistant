package nl.andrewlalis.teaching_assistant_assistant.model.people;

import nl.andrewlalis.teaching_assistant_assistant.model.assignments.grades.SectionGrade;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * Represents a teaching assistant of a course, or in other words, a grader and administrator.
 */
@Entity
public class TeachingAssistant extends Person {

    /**
     * The list of all feedback given by a teaching assistant.
     */
    @OneToMany
    @JoinColumn(name = "teaching_assistant_id")
    private List<SectionGrade> sectionGrades;

}
