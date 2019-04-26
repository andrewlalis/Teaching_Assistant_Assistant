package nl.andrewlalis.teaching_assistant_assistant.model.people.teams;

import nl.andrewlalis.teaching_assistant_assistant.model.Course;
import nl.andrewlalis.teaching_assistant_assistant.model.assignments.grades.AssignmentGrade;
import nl.andrewlalis.teaching_assistant_assistant.model.people.Person;
import nl.andrewlalis.teaching_assistant_assistant.model.people.Student;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A group of students.
 */
@Entity
public class StudentTeam extends Team {

    /**
     * The name of the github repository that belongs to this team.
     */
    @Column
    private String githubRepositoryName;

    /**
     * The teaching assistant team to which this student team is assigned.
     */
    @ManyToOne(
            fetch = FetchType.LAZY
    )
    private TeachingAssistantTeam assignedTeachingAssistantTeam;

    /**
     * The list of assignment grades which this student group has received.
     */
    @OneToMany(
            fetch = FetchType.LAZY
    )
    private List<AssignmentGrade> assignmentGrades;

    /**
     * Default constructor for JPA.
     */
    public StudentTeam() {}

    public StudentTeam(Course course) {
        super(course);
    }

    public List<Student> getStudents() {
        List<Person> people = super.getMembers();
        List<Student> students = new ArrayList<>();
        people.forEach(person -> {
            students.add((Student) person);
        });
        return students;
    }

    public String getGithubRepositoryName() {
        return this.githubRepositoryName;
    }

    public void setGithubRepositoryName(String name) {
        this.githubRepositoryName = name;
    }

    public TeachingAssistantTeam getAssignedTeachingAssistantTeam() {
        return this.assignedTeachingAssistantTeam;
    }

    public void setAssignedTeachingAssistantTeam(TeachingAssistantTeam team) {
        this.assignedTeachingAssistantTeam = team;
    }

}
