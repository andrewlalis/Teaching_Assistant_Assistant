package nl.andrewlalis.teaching_assistant_assistant.model.assignments;

import nl.andrewlalis.teaching_assistant_assistant.model.BasicEntity;

import javax.persistence.*;

/**
 * The feedback given by a teaching assistant to a student or group regarding a particular section of an assignment.
 */
@Entity
@Inheritance(
        strategy = InheritanceType.JOINED
)
public abstract class Feedback extends BasicEntity {

    /**
     * The section to which this feedback belongs.
     */
    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    private AssignmentSection assignmentSection;



}
