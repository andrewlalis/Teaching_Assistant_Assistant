package nl.andrewlalis.teaching_assistant_assistant.services;

import nl.andrewlalis.teaching_assistant_assistant.model.Course;
import nl.andrewlalis.teaching_assistant_assistant.model.people.Student;
import nl.andrewlalis.teaching_assistant_assistant.model.repositories.CourseRepository;
import nl.andrewlalis.teaching_assistant_assistant.model.repositories.StudentRepository;
import org.springframework.stereotype.Service;

/**
 * Helps with manipulation and various operations on individual students or groups of students (not teams).
 */
@Service
public class StudentService {

    private StudentRepository studentRepository;
    private CourseRepository courseRepository;

    protected StudentService (StudentRepository studentRepository, CourseRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
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
            this.courseRepository.save(course);
            this.studentRepository.save(student);
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

}
