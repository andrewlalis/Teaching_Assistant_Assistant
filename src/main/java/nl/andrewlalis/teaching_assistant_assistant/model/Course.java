package nl.andrewlalis.teaching_assistant_assistant.model;

import nl.andrewlalis.teaching_assistant_assistant.model.assignments.Assignment;
import nl.andrewlalis.teaching_assistant_assistant.model.people.Person;
import nl.andrewlalis.teaching_assistant_assistant.model.people.Student;
import nl.andrewlalis.teaching_assistant_assistant.model.people.TeachingAssistant;
import nl.andrewlalis.teaching_assistant_assistant.model.people.teams.StudentTeam;
import nl.andrewlalis.teaching_assistant_assistant.model.people.teams.TeachingAssistantTeam;
import nl.andrewlalis.teaching_assistant_assistant.model.people.teams.Team;

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
     * The github organization name for this course, if any.
     */
    @Column
    private String githubOrganizationName;

    /**
     * The API key that will be used for Github interaction with this organization.
     */
    @Column
    private String apiKey;

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
     * The list of all participants in this course, both teaching assistants and students.
     */
    @ManyToMany(
            cascade = CascadeType.ALL
    )
    @JoinTable(
            name = "course_participants",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "person_id")
    )
    private List<Person> participants;

    /**
     * Default constructor for JPA.
     */
    protected Course() {
        this.assignments = new ArrayList<>();
        this.studentTeams = new ArrayList<>();
        this.teachingAssistantTeams = new ArrayList<>();
        this.participants = new ArrayList<>();
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

    public void addStudentTeam(StudentTeam team) {
        this.studentTeams.add(team);
    }

    public void removeStudentTeam(StudentTeam team) {
        this.studentTeams.remove(team);
    }

    public void addTeachingAssistantTeam(TeachingAssistantTeam team) {
        this.teachingAssistantTeams.add(team);
    }

    public void removeTeachingAssistantTeam(TeachingAssistantTeam team) {
        this.teachingAssistantTeams.remove(team);
    }

    public void addParticipant(Person person) {
        if (!this.participants.contains(person)) {
            this.participants.add(person);
        }
    }

    public void removeParticipant(Person person) {
        this.participants.remove(person);
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

    public String getGithubOrganizationName() {
        return this.githubOrganizationName;
    }

    public void setGithubOrganizationName(String name) {
        this.githubOrganizationName = name;
    }

    public String getApiKey() {
        return this.apiKey;
    }

    public void setApiKey(String key) {
        this.apiKey = key;
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

    public List<Student> getStudents() {
        List<Student> students = new ArrayList<>();
        this.participants.forEach(participant -> {
            if (participant instanceof Student) {
                students.add((Student) participant);
            }
        });
        return students;
    }

    public List<TeachingAssistant> getTeachingAssistants() {
        List<TeachingAssistant> teachingAssistants = new ArrayList<>();
        this.participants.forEach(participant -> {
            if (participant instanceof TeachingAssistant) {
                teachingAssistants.add((TeachingAssistant) participant);
            }
        });
        return teachingAssistants;
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

    public int getNumberOfStudentsInTeams() {
        int sum = 0;
        for (Student s : this.getStudents()) {
            for (Team team : s.getTeams()) {
                if (team.getCourse().equals(this)) {
                    sum++;
                    break;
                }
            }
        }

        return sum;
    }
}
