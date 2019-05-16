package nl.andrewlalis.teaching_assistant_assistant.controllers;

import nl.andrewlalis.teaching_assistant_assistant.model.dto.NewUserRegistrationDTO;
import nl.andrewlalis.teaching_assistant_assistant.model.repositories.CourseRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;

/**
 * Controller for the registration page which is used for the creation of new accounts.
 */
@Controller
public class RegisterController {

    private final Logger logger = LogManager.getLogger(RegisterController.class);

    private CourseRepository courseRepository;

    private static final String[] personTypes = {
            "Student",
            "Teaching Assistant",
            "Professor",
            "Administrator"
    };

    protected RegisterController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @GetMapping("/register")
    public String get(Model model) {
        model.addAttribute("newUserRegistration", new NewUserRegistrationDTO(Arrays.asList(personTypes), this.courseRepository.findAll()));
        return "register";
    }

    @PostMapping("/register")
    public String post(@ModelAttribute NewUserRegistrationDTO newUserRegistration) {
        logger.info("Received new registration: " + newUserRegistration);

        return "redirect:/login";
    }
}
