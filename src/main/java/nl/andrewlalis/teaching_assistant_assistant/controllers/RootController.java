package nl.andrewlalis.teaching_assistant_assistant.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RootController {

    @RequestMapping(
            path = "/",
            produces = "text/html"
    )
    public String index(Model model) {
        model.addAttribute("name", "JOHN");
        return "index.html";
    }

}
