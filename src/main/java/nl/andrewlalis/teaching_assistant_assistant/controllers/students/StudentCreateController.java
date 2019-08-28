package nl.andrewlalis.teaching_assistant_assistant.controllers.students;

import nl.andrewlalis.teaching_assistant_assistant.controllers.UserPageController;
import nl.andrewlalis.teaching_assistant_assistant.model.Course;
import nl.andrewlalis.teaching_assistant_assistant.model.people.Student;
import nl.andrewlalis.teaching_assistant_assistant.model.repositories.CourseRepository;
import nl.andrewlalis.teaching_assistant_assistant.services.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

/**
 * Controller for creating a new student.
 */
@Controller
public class StudentCreateController extends UserPageController {

    /**
     * A constant which defines what value is returned if the user says that the newly created student should not be
     * part of a course.
     */
    private static final String NO_COURSE = "NO_COURSE_SELECTED";

    private CourseRepository courseRepository;
    private StudentService studentService;

    protected StudentCreateController(CourseRepository courseRepository, StudentService studentService) {
        this.courseRepository = courseRepository;
        this.studentService = studentService;
    }

    @GetMapping("/students/create")
    public String getCreate(Model model) {
        model.addAttribute("student", new Student(null, null, null, null, 1234567));
        model.addAttribute("courses", this.courseRepository.findAll());

        return "students/create";
    }

    @PostMapping(
            value = "/students/create",
            consumes = "application/x-www-form-urlencoded"
    )
    public String postCreate(
            @ModelAttribute Student newStudent,
            @RequestParam(value = "course_code", required = false, defaultValue = NO_COURSE) String courseCode
    ) {
        Optional<Course> optionalCourse = this.courseRepository.findByCode(courseCode);
        Course course = null;
        if (optionalCourse.isPresent()) {
            course = optionalCourse.get();
        }

        this.studentService.createStudent(newStudent, course);

        return "redirect:/students";
    }
}
