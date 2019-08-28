package nl.andrewlalis.teaching_assistant_assistant.controllers.courses;

import nl.andrewlalis.teaching_assistant_assistant.controllers.UserPageController;
import nl.andrewlalis.teaching_assistant_assistant.model.Course;
import nl.andrewlalis.teaching_assistant_assistant.model.repositories.CourseRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CreateCourse extends UserPageController {

    private CourseRepository courseRepository;

    protected CreateCourse(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @GetMapping("/courses/create")
    public String get(Model model) {
        Course course = new Course(null, null);
        model.addAttribute("course", course);
        return "courses/create";
    }
}
