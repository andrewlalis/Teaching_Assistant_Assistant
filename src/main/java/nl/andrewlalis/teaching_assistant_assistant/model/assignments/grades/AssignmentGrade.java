package nl.andrewlalis.teaching_assistant_assistant.model.assignments.grades;

import nl.andrewlalis.teaching_assistant_assistant.model.BasicEntity;
import nl.andrewlalis.teaching_assistant_assistant.model.assignments.Assignment;

import javax.persistence.*;
import java.util.List;

/**
 * Represents a grade for a certain student's submission for an assignment.
 */
@Entity
public class AssignmentGrade extends BasicEntity {

    /**
     * The assignment to which this grade belongs.
     */
    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    private Assignment assignment;

    /**
     * The list of section grades which make up this total grade.
     */
    @OneToMany(
            fetch = FetchType.EAGER,
            orphanRemoval = true
    )
    @JoinColumn(name = "assignmentGrade_id")
    private List<SectionGrade> sectionGrades;

}
