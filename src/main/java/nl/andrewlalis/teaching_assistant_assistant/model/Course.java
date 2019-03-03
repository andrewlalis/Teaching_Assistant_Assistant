package nl.andrewlalis.teaching_assistant_assistant.model;

import nl.andrewlalis.teaching_assistant_assistant.model.assignments.Assignment;
import nl.andrewlalis.teaching_assistant_assistant.model.people.Student;
import nl.andrewlalis.teaching_assistant_assistant.model.people.TeachingAssistant;

import javax.persistence.*;
import java.util.List;

/**
 * Represents a course, containing many students and teaching assistants, as well as a collection of assignments.
 */
@Entity
@Inheritance(
        strategy = InheritanceType.JOINED
)
public class Course extends BasicEntity {

    /**
     * The human-readable name for this course.
     */
    @Column(unique = true, nullable = false)
    private String name;

    /**
     * The unique identifying code for this course.
     */
    @Column(unique = true, nullable = false)
    private String code;

    /**
     * The list of students participating in this course. This is a many-to-many relationship because a course can
     * contain multiple students, and a student can participate in multiple courses at once.
     */
    @ManyToMany
    @JoinTable(
            name = "course_student",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<Student> students;

    /**
     * The list of teaching assistants managing this course. Just like for students, this is a many-to-many relation.
     */
    @ManyToMany
    @JoinTable(
            name = "course_teachingAssistant",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "teachingAssistant_id")
    )
    private List<TeachingAssistant> teachingAssistants;

    /**
     * The list of assignments this course contains.
     */
    @OneToMany(
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    @JoinColumn(name = "course_id")
    private List<Assignment> assignments;

    /**
     * Default constructor for JPA.
     */
    protected Course() {}

    /**
     * Constructs a new Course object.
     * @param name The name of the course.
     * @param code The course's unique code.
     */
    public Course(String name, String code) {
        this.name = name;
        this.code = code;
    }

    /*
    Getters and Setters
     */

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public List<TeachingAssistant> getTeachingAssistants() {
        return teachingAssistants;
    }

    public List<Student> getStudents() {
        return students;
    }

    public List<Assignment> getAssignments() {
        return assignments;
    }
}
