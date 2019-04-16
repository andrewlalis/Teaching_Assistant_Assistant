package nl.andrewlalis.teaching_assistant_assistant.controllers.courses;

import nl.andrewlalis.teaching_assistant_assistant.model.Course;
import nl.andrewlalis.teaching_assistant_assistant.model.repositories.CourseRepository;
import nl.andrewlalis.teaching_assistant_assistant.util.sample_data.CourseGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class Generate {

    private CourseRepository courseRepository;

    protected Generate(CourseRepository courseRepository) {
         this.courseRepository = courseRepository;
    }

    @GetMapping("/courses/generate")
    public String get(Model model) {
        CourseGenerator courseGenerator = new CourseGenerator(0, 3, 2, 10, 3);

        List<Course> courses = courseGenerator.generateList(5);
        this.courseRepository.saveAll(courses);

        model.addAttribute("courses", courseRepository.findAll());
        return "courses";
    }

}
