package nl.andrewlalis.teaching_assistant_assistant.controllers;

import nl.andrewlalis.teaching_assistant_assistant.model.Course;
import nl.andrewlalis.teaching_assistant_assistant.model.people.TeachingAssistant;
import nl.andrewlalis.teaching_assistant_assistant.model.repositories.CourseRepository;
import nl.andrewlalis.teaching_assistant_assistant.model.repositories.TeachingAssistantRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

/**
 * Controller for the list of teaching assistants in the system.
 */
@Controller
public class TeachingAssistantsController {

    private TeachingAssistantRepository teachingAssistantRepository;
    private CourseRepository courseRepository;

    protected TeachingAssistantsController(TeachingAssistantRepository teachingAssistantRepository, CourseRepository courseRepository) {
        this.teachingAssistantRepository = teachingAssistantRepository;
        this.courseRepository = courseRepository;
    }

    @GetMapping("/teaching_assistants")
    public String get(Model model) {
        model.addAttribute("teaching_assistants", teachingAssistantRepository.findAll());
        return "teaching_assistants";
    }

    @GetMapping("/courses/{code}/teaching_assistants/create")
    public String getCreate(@PathVariable String code, Model model) {
        model.addAttribute("teachingAssistant", new TeachingAssistant("First Name", "Last Name", "github Username", "me@example.com"));
        Optional<Course> optionalCourse = this.courseRepository.findByCode(code);
        optionalCourse.ifPresent(course -> model.addAttribute("course", course));
        return "courses/entity/teaching_assistants/create";
    }

    @PostMapping(
            value = "/courses/{code}/teaching_assistants",
            consumes = "application/x-www-form-urlencoded"
    )
    public String post(@PathVariable String code, @ModelAttribute("teachingAssistant") TeachingAssistant teachingAssistant) {
        Optional<Course> optionalCourse = this.courseRepository.findByCode(code);
        optionalCourse.ifPresent(course -> {
            course.addParticipant(teachingAssistant);
            this.courseRepository.save(course);
        });
        return "redirect:/teaching_assistants";
    }
}
