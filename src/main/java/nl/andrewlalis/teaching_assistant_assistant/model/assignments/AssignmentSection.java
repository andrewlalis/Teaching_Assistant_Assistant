package nl.andrewlalis.teaching_assistant_assistant.model.assignments;

import nl.andrewlalis.teaching_assistant_assistant.model.BasicEntity;

import javax.persistence.*;

/**
 * An Assignment is comprised of one or more sections, each of which has its own grade and feedback.
 */
@Entity
@Inheritance(
        strategy = InheritanceType.JOINED
)
public class AssignmentSection extends BasicEntity {

    /**
     * The parent assignment to which this section belongs.
     */
    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    private Assignment assignment;

}
