package nl.andrewlalis.teaching_assistant_assistant.controllers.register;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for the registration form for new students.
 */
@Controller
public class StudentRegisterController {

    @GetMapping("/register/student")
    public String get() {


        return "/register/student";
    }

}
