package nl.andrewlalis.teaching_assistant_assistant.controllers;

import nl.andrewlalis.teaching_assistant_assistant.model.Course;
import nl.andrewlalis.teaching_assistant_assistant.model.people.Student;
import nl.andrewlalis.teaching_assistant_assistant.model.repositories.CourseRepository;
import nl.andrewlalis.teaching_assistant_assistant.model.repositories.StudentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

/**
 * Controller for operations dealing with the global collection of students, not particular to one course.
 */
@Controller
public class Students {

    private static final String NO_COURSE = "NO_COURSE_SELECTED";

    private StudentRepository studentRepository;
    private CourseRepository courseRepository;

    protected Students(StudentRepository studentRepository, CourseRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @GetMapping("/students")
    public String get(Model model) {
        model.addAttribute("students", this.studentRepository.findAll());
        return "students";
    }

    @GetMapping("/students/create")
    public String getCreate(Model model) {
        model.addAttribute("student", new Student("First Name", "Last Name", "Email Address", "Github Username", 1234567));
        model.addAttribute("courses", this.courseRepository.findAll());

        return "students/create";
    }

    @PostMapping(
            value = "/students/create",
            consumes = "application/x-www-form-urlencoded"
    )
    public String postCreate(
            @ModelAttribute Student newStudent,
            @RequestParam(value = "course_code", required = false) String courseCode
    ) {
        this.studentRepository.save(newStudent);

        if (courseCode != null && !courseCode.equals(NO_COURSE)) {
            Optional<Course> optionalCourse = this.courseRepository.findByCode(courseCode);
            optionalCourse.ifPresent(course -> {
                course.addParticipant(newStudent);
                newStudent.assignToCourse(course);
                this.courseRepository.save(course);
                this.studentRepository.save(newStudent);
            });
        }

        return "redirect:/students";
    }
}
