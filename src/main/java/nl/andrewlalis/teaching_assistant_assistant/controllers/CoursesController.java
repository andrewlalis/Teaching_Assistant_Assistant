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
public class CoursesController {

    private CourseRepository courseRepository;

    protected CoursesController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    /**
     * Handles requests to get the list of courses.
     * @param model The view model to populate with data for the template.
     * @return The name of the template that will be used with the given model to build the view.
     */
    @GetMapping("/courses")
    public String get(Model model) {
        model.addAttribute("courses", courseRepository.findAll());
        return "courses";
    }

    /**
     * Handles POST requests to this collection of courses. This facilitates the creation of new courses.
     * @param course The course which has been submitted by a user.
     * @return The entity view for the course which has just been created.
     */
    @PostMapping(
            value = "/courses",
            consumes = "application/x-www-form-urlencoded"
    )
    public String post(@ModelAttribute Course course) {
        this.courseRepository.save(course);
        return "courses/entity";
    }

}
