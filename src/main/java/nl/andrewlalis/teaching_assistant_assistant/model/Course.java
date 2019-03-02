package nl.andrewlalis.teaching_assistant_assistant.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 * Represents a course, containing many students and teaching assistants, as well as a collection of assignments.
 */
@Entity
@Inheritance(
        strategy = InheritanceType.JOINED
)
public class Course extends BasicEntity {

    @Column(unique = true, nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String code;

    /**
     * Default constructor for JPA.
     */
    protected Course() {}

}
