package nl.andrewlalis.teaching_assistant_assistant.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController extends UserPageController {

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }
}
