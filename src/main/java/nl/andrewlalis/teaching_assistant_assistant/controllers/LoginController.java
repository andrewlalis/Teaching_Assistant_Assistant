package nl.andrewlalis.teaching_assistant_assistant.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for the login page, visible to all users.
 */
@Controller
public class LoginController {

    private final Logger logger = LogManager.getLogger(LoginController.class);

    @GetMapping("/login")
    public String get() {
        logger.info("User got login page.");

        return "login";
    }
}
