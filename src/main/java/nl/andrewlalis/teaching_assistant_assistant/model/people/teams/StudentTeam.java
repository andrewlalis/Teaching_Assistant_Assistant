package nl.andrewlalis.teaching_assistant_assistant.model.people.teams;

import nl.andrewlalis.teaching_assistant_assistant.model.assignments.grades.AssignmentGrade;
import nl.andrewlalis.teaching_assistant_assistant.model.people.Person;
import nl.andrewlalis.teaching_assistant_assistant.model.people.Student;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * A group of students.
 */
@Entity
public class StudentTeam extends Team {

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

    public List<Student> getStudents() {
        List<Person> people = super.getMembers();
        List<Student> students = new ArrayList<>();
        people.forEach(person -> {
            students.add((Student) person);
        });
        return students;
    }

}
