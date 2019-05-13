package nl.andrewlalis.teaching_assistant_assistant.controllers.students.entity;

import nl.andrewlalis.teaching_assistant_assistant.model.people.Student;
import nl.andrewlalis.teaching_assistant_assistant.model.repositories.StudentRepository;
import nl.andrewlalis.teaching_assistant_assistant.services.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

/**
 * Controller for removing a student from the application.
 */
@Controller
public class StudentEntityRemoveController {

    private StudentService studentService;
    private StudentRepository studentRepository;

    protected StudentEntityRemoveController(StudentService studentService, StudentRepository studentRepository) {
        this.studentService = studentService;
        this.studentRepository = studentRepository;
    }

    @GetMapping("/students/{id}/remove")
    public String getRemove(@PathVariable long id) {
        Optional<Student> optionalStudent = this.studentRepository.findById(id);
        optionalStudent.ifPresent(student -> this.studentService.removeStudent(student));

        return "redirect:/students";
    }

}
