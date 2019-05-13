package nl.andrewlalis.teaching_assistant_assistant.controllers.students;

import nl.andrewlalis.teaching_assistant_assistant.model.people.Student;
import nl.andrewlalis.teaching_assistant_assistant.model.repositories.StudentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

/**
 * Controller for a single student entity.
 */
@Controller
public class StudentEntityController {

    private StudentRepository studentRepository;

    protected StudentEntityController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @GetMapping("/students/{id}")
    public String get(@PathVariable long id, Model model) {
        Optional<Student> optionalStudent = this.studentRepository.findById(id);
        optionalStudent.ifPresent(student -> model.addAttribute("student", student));
        return "students/entity";
    }
}
