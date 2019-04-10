package nl.andrewlalis.teaching_assistant_assistant.model;

import nl.andrewlalis.teaching_assistant_assistant.model.assignments.Assignment;
import nl.andrewlalis.teaching_assistant_assistant.model.people.teams.StudentTeam;
import nl.andrewlalis.teaching_assistant_assistant.model.people.teams.TeachingAssistantTeam;

import javax.persistence.*;
import java.util.ArrayList;
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
     * The list of assignments this course contains.
     */
    @OneToMany(
            orphanRemoval = true,
            cascade = CascadeType.ALL
    )
    @JoinColumn(name = "course_id")
    private List<Assignment> assignments;

    /**
     * The list of student teams in this course.
     */
    @OneToMany(
            orphanRemoval = true,
            cascade = CascadeType.ALL
    )
    private List<StudentTeam> studentTeams;

    /**
     * The list of teaching assistant teams that belong to this course.
     */
    @OneToMany(
            orphanRemoval = true,
            cascade = CascadeType.ALL
    )
    private List<TeachingAssistantTeam> teachingAssistantTeams;

    /**
     * Default constructor for JPA.
     */
    protected Course() {
        this.assignments = new ArrayList<>();
        this.studentTeams = new ArrayList<>();
        this.teachingAssistantTeams = new ArrayList<>();
    }

    /**
     * Constructs a new Course object.
     * @param name The name of the course.
     * @param code The course's unique code.
     */
    public Course(String name, String code) {
        this(); // Initialize all array lists first.
        this.name = name;
        this.code = code;
    }

    public void addStudentGroup(StudentTeam group) {
        this.studentTeams.add(group);
    }

    public void addTeachingAssistantGroup(TeachingAssistantTeam group) {
        this.teachingAssistantTeams.add(group);
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

    public List<Assignment> getAssignments() {
        return assignments;
    }

    public List<StudentTeam> getStudentTeams() {
        return studentTeams;
    }

    public List<TeachingAssistantTeam> getTeachingAssistantTeams() {
        return teachingAssistantTeams;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getName()).append('\n');
        sb.append("TA teams: \n");
        for (TeachingAssistantTeam team : this.getTeachingAssistantTeams()) {
            sb.append(team.toString()).append('\n');
        }
        sb.append("Student teams: \n");
        for (StudentTeam team : this.getStudentTeams()) {
            sb.append(team.toString()).append('\n');
        }
        return sb.toString();
    }
}
