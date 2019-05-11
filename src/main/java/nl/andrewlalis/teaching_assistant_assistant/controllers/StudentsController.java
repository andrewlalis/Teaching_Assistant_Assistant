package nl.andrewlalis.teaching_assistant_assistant.controllers;

import nl.andrewlalis.teaching_assistant_assistant.model.repositories.StudentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for operations dealing with the global collection of students, not particular to one course.
 */
@Controller
public class StudentsController {

    private StudentRepository studentRepository;

    protected StudentsController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    /**
     * Gets a list of all students.
     * @param model The view model.
     * @return The template for displaying a list of students.
     */
    @GetMapping("/students")
    public String get(Model model) {
        model.addAttribute("students", this.studentRepository.findAll());
        return "students";
    }
}
