package nl.andrewlalis.teaching_assistant_assistant.controllers.courses;

import nl.andrewlalis.teaching_assistant_assistant.controllers.UserPageController;
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
public class CourseEntity extends UserPageController {

    private CourseRepository courseRepository;

    protected CourseEntity(CourseRepository courseRepository) {
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
        courseOptional.ifPresent(course -> model.addAttribute("course", course));
        return "courses/entity";
    }

    /**
     * Gets the student teams for a particular course.
     * @param code The course code.
     * @param model The view model.
     * @return The template for viewing the list of student teams.
     */
    @GetMapping("/courses/{code}/student_teams")
    public String getStudentTeams(@PathVariable String code, Model model) {
        Optional<Course> courseOptional = this.courseRepository.findByCode(code);
        courseOptional.ifPresent(course -> model.addAttribute("course", course));
        return "courses/entity/student_teams";
    }

    /**
     * Gets the teaching assistant teams for a particular course.
     * @param code The course code.
     * @param model The view model.
     * @return The template for viewing the list of student teams.
     */
    @GetMapping("/courses/{code}/teaching_assistant_teams")
    public String getTeachingAssistantTeams(@PathVariable String code, Model model) {
        Optional<Course> courseOptional = this.courseRepository.findByCode(code);
        courseOptional.ifPresent(course -> model.addAttribute("course", course));
        return "courses/entity/teaching_assistant_teams";
    }

    /**
     * Gets the students for a particular course.
     * @param code The course code.
     * @param model The view model.
     * @return The template for viewing the list of students.
     */
    @GetMapping("/courses/{code}/students")
    public String getStudents(@PathVariable String code, Model model) {
        Optional<Course> courseOptional = this.courseRepository.findByCode(code);
        courseOptional.ifPresent(course -> model.addAttribute("course", course));
        return "courses/entity/students";
    }

    /**
     * Gets the teaching assistants for a particular course.
     * @param code The course code.
     * @param model The view model.
     * @return The template for viewing the list of teaching assistants.
     */
    @GetMapping("/courses/{code}/teaching_assistants")
    public String getTeachingAssistants(@PathVariable String code, Model model) {
        Optional<Course> courseOptional = this.courseRepository.findByCode(code);
        courseOptional.ifPresent(course -> model.addAttribute("course", course));
        return "courses/entity/teaching_assistants";
    }

}
