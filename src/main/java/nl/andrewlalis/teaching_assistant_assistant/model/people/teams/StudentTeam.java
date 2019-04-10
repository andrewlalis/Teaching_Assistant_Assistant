package nl.andrewlalis.teaching_assistant_assistant.model.people.teams;

import nl.andrewlalis.teaching_assistant_assistant.model.assignments.grades.AssignmentGrade;
import nl.andrewlalis.teaching_assistant_assistant.model.people.Student;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * A group of students.
 */
@Entity
public class StudentTeam extends Team<Student> {

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

    @Override
    public void addMember(Student person) {
        this.getMembers().add(person);
    }
}
