package nl.andrewlalis.teaching_assistant_assistant.services;

import nl.andrewlalis.teaching_assistant_assistant.model.Course;
import nl.andrewlalis.teaching_assistant_assistant.model.people.Student;
import nl.andrewlalis.teaching_assistant_assistant.model.people.teams.Team;
import nl.andrewlalis.teaching_assistant_assistant.model.repositories.CourseRepository;
import nl.andrewlalis.teaching_assistant_assistant.model.repositories.StudentRepository;
import nl.andrewlalis.teaching_assistant_assistant.model.repositories.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Helps with manipulation and various operations on individual students or groups of students (not teams).
 */
@Service
public class StudentService {

    private StudentRepository studentRepository;
    private CourseRepository courseRepository;
    private TeamRepository teamRepository;

    protected StudentService (StudentRepository studentRepository, CourseRepository courseRepository, TeamRepository teamRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.teamRepository = teamRepository;
    }

    /**
     * Creates a new student and assigns them to a course, if provided.
     * @param student An unsaved student model.
     * @param course The course to assign the student to. This may be null.
     */
    public void createStudent(Student student, Course course) {
        if (course != null) {
            course.addParticipant(student);
            student.assignToCourse(course);
            this.courseRepository.save(course); // This cascades to save the student as well.
        }
    }

    /**
     * Edits a students' data. More specifically, updates the provided <code>student</code> with the attributes found
     * in the provided <code>editedStudent</code> object.
     * @param student The student to update.
     * @param editedStudent A model containing updated attributes to assign to the given student.
     */
    public void editStudent(Student student, Student editedStudent) {
        student.setFirstName(editedStudent.getFirstName());
        student.setLastName(editedStudent.getLastName());
        student.setStudentNumber(editedStudent.getStudentNumber());
        student.setEmailAddress(editedStudent.getEmailAddress());
        student.setGithubUsername(editedStudent.getGithubUsername());
        this.studentRepository.save(student);
    }

    /**
     * Removes a student from the application completely.
     * @param student The student to remove.
     */
    public void removeStudent(Student student) {
        List<Team> teams = student.getTeams();
        for (Team team : teams) {
            team.removeMember(student);
            student.removeFromAssignedTeam(team);
            this.teamRepository.save(team);
        }

        List<Course> courses = student.getCourses();
        for (Course course : courses) {
            course.removeParticipant(student);
            student.removeFromAssignedCourse(course);
            this.courseRepository.save(course);
        }

        this.studentRepository.delete(student);
    }

}
