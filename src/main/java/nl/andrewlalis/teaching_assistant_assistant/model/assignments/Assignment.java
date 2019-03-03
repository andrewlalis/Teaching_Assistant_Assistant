package nl.andrewlalis.teaching_assistant_assistant.model.assignments;

import nl.andrewlalis.teaching_assistant_assistant.model.BasicEntity;
import nl.andrewlalis.teaching_assistant_assistant.model.Course;

import javax.persistence.*;
import java.util.List;

/**
 * Represents a single assignment for a course.
 */
@Entity
@Inheritance(
        strategy = InheritanceType.JOINED
)
public class Assignment extends BasicEntity {

    /**
     * The parent course to which this assignment belongs.
     */
    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    private Course course;

    /**
     * The list of sections which comprise this assignment.
     */
    @OneToMany(
            fetch = FetchType.EAGER,
            orphanRemoval = true
    )
    @JoinColumn(name = "assignment_id")
    private List<AssignmentSection> sections;


}
