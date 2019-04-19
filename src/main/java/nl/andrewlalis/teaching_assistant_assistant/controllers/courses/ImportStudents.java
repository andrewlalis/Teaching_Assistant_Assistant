package nl.andrewlalis.teaching_assistant_assistant.controllers.courses;

import nl.andrewlalis.teaching_assistant_assistant.model.Course;
import nl.andrewlalis.teaching_assistant_assistant.model.people.teams.StudentTeam;
import nl.andrewlalis.teaching_assistant_assistant.model.repositories.CourseRepository;
import nl.andrewlalis.teaching_assistant_assistant.model.repositories.StudentRepository;
import nl.andrewlalis.teaching_assistant_assistant.model.repositories.StudentTeamRepository;
import nl.andrewlalis.teaching_assistant_assistant.util.team_importing.StudentTeamImporter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Controller for importing students from a CSV sheet.
 */
@Controller
public class ImportStudents {

    private CourseRepository courseRepository;

    private StudentTeamRepository studentTeamRepository;

    private StudentRepository studentRepository;

    protected ImportStudents(CourseRepository courseRepository, StudentTeamRepository studentTeamRepository, StudentRepository studentRepository) {
        this.courseRepository = courseRepository;
        this.studentTeamRepository = studentTeamRepository;
        this.studentRepository = studentRepository;
    }

    @GetMapping("/courses/{code}/import_students")
    public String get(@PathVariable String code, Model model) {
        Optional<Course> optionalCourse = this.courseRepository.findByCode(code);
        optionalCourse.ifPresent(course -> model.addAttribute("course", course));
        return "courses/import_students";
    }

    @PostMapping(
            value = "/courses/{code}/import_students"
    )
    public String post(@PathVariable String code, @RequestParam MultipartFile file) {
        Optional<Course> optionalCourse = this.courseRepository.findByCode(code);

        if (!optionalCourse.isPresent()) {
            System.out.println("No course found.");
            return "redirect:/courses";
        }

        Course course = optionalCourse.get();

        try {
            List<StudentTeam> studentTeams = StudentTeamImporter.importFromCSV(file.getInputStream(), optionalCourse.get());

            // Save all the new students first, then save all the teams they belong to.
            for (StudentTeam team : studentTeams) {
                team.getMembers().forEach(student -> student.assignToCourse(course));
                this.studentRepository.saveAll(team.getStudents());
                team.setCourse(course);
            }

            studentTeams.forEach(team -> {
                team.getMembers().forEach(student -> {
                    student.assignToCourse(course);
                    course.addParticipant(student);
                });
                team.setCourse(course);
                course.addStudentTeam(team);

                //this.studentRepository.saveAll((team.getMembers()));
            });

            //this.studentTeamRepository.saveAll(studentTeams);

            this.courseRepository.save(course);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/courses/{code}";
    }
}
