package nl.andrewlalis.teaching_assistant_assistant.controllers;

import nl.andrewlalis.teaching_assistant_assistant.model.repositories.StudentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Students {

    private StudentRepository studentRepository;

    protected Students(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @GetMapping("/students")
    public String get(Model model) {
        model.addAttribute("students", this.studentRepository.findAll());
        return "students";
    }
}
