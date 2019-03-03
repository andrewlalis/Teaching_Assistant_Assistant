package nl.andrewlalis.teaching_assistant_assistant.controllers.courses;

import nl.andrewlalis.teaching_assistant_assistant.model.Course;
import nl.andrewlalis.teaching_assistant_assistant.model.repositories.CourseRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class Entity {

    private CourseRepository courseRepository;

    protected Entity(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @GetMapping("/courses/{id}")
    public String get(@PathVariable Long id, Model model) {
        Optional<Course> courseOptional = this.courseRepository.findById(id);
        courseOptional.ifPresent(course -> model.addAttribute("course", course));

        return "courses/entity";
    }

}
