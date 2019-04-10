package nl.andrewlalis.teaching_assistant_assistant.controllers;

import nl.andrewlalis.teaching_assistant_assistant.model.Course;
import nl.andrewlalis.teaching_assistant_assistant.model.repositories.CourseRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Controller for the list of courses in the system.
 */
@Controller
public class Courses {

    private CourseRepository courseRepository;

    protected Courses(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @GetMapping("/courses")
    public String get(Model model) {
        model.addAttribute("courses", courseRepository.findAll());
        return "courses";
    }

    @PostMapping(
            value = "/courses",
            consumes = "application/json"
    )
    public String post(@ModelAttribute Course course) {
        System.out.println("Object submitted: " + course);
        this.courseRepository.save(course);
        return "/courses/created";
    }

}
