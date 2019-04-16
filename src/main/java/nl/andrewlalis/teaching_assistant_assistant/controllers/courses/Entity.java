package nl.andrewlalis.teaching_assistant_assistant.controllers.courses;

import nl.andrewlalis.teaching_assistant_assistant.model.Course;
import nl.andrewlalis.teaching_assistant_assistant.model.repositories.CourseRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

/**
 * Controller for the course entity, that is, one individual course.
 */
@Controller
public class Entity {

    private CourseRepository courseRepository;

    protected Entity(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    /**
     * Handles get requests to a course with a given code.
     * @param code The unique course code used to identify a course entity.
     * @param model The view model that will be populated with data.
     * @return The template which will be used in conjunction with the model to build a view.
     */
    @GetMapping("/courses/{code}")
    public String get(@PathVariable String code, Model model) {
        Optional<Course> courseOptional = this.courseRepository.findByCode(code);

        if (courseOptional.isPresent()) {
            Course course = courseOptional.get();
            model.addAttribute("course", course);
        }

        return "courses/entity";
    }

}
