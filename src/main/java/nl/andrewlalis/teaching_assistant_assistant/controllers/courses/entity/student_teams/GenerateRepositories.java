package nl.andrewlalis.teaching_assistant_assistant.controllers.courses.entity.student_teams;

import nl.andrewlalis.teaching_assistant_assistant.model.Course;
import nl.andrewlalis.teaching_assistant_assistant.model.repositories.CourseRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class GenerateRepositories {

    private CourseRepository courseRepository;

    protected GenerateRepositories(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @GetMapping("/courses/{courseCode}/student_teams/generate_repositories")
    public String get(@PathVariable String courseCode, Model model) {
        Optional<Course> optionalCourse = this.courseRepository.findByCode(courseCode);
        optionalCourse.ifPresent(course -> model.addAttribute("course", course));

        return "courses/entity/student_teams/generate_repositories";
    }

    @PostMapping(
            value = "/courses/{courseCode}/student_teams/generate_repositories",
            consumes = "application/x-www-form-urlencoded"
    )
    public String post(@PathVariable String courseCode) {
        System.out.println("Post received for " + courseCode);

        return "redirect:/courses/{courseCode}";
    }
}
