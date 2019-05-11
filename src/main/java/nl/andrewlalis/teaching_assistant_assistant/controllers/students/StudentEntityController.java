package nl.andrewlalis.teaching_assistant_assistant.controllers.students;

import nl.andrewlalis.teaching_assistant_assistant.model.Course;
import nl.andrewlalis.teaching_assistant_assistant.model.people.Student;
import nl.andrewlalis.teaching_assistant_assistant.model.people.teams.Team;
import nl.andrewlalis.teaching_assistant_assistant.model.repositories.CourseRepository;
import nl.andrewlalis.teaching_assistant_assistant.model.repositories.StudentRepository;
import nl.andrewlalis.teaching_assistant_assistant.model.repositories.TeamRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

/**
 * Controller for a single student entity.
 */
@Controller
public class StudentEntityController {

    private StudentRepository studentRepository;
    private TeamRepository teamRepository;
    private CourseRepository courseRepository;

    protected StudentEntityController(StudentRepository studentRepository, TeamRepository teamRepository, CourseRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.teamRepository = teamRepository;
        this.courseRepository = courseRepository;
    }

    @GetMapping("/students/{id}")
    public String get(@PathVariable long id, Model model) {
        Optional<Student> optionalStudent = this.studentRepository.findById(id);
        optionalStudent.ifPresent(student -> model.addAttribute("student", student));
        return "students/entity";
    }



    @GetMapping("/students/{id}/remove")
    public String getRemove(@PathVariable long id) {
        Optional<Student> optionalStudent = this.studentRepository.findById(id);
        optionalStudent.ifPresent(student -> {

            for (Team team : student.getTeams()) {
                team.removeMember(student);
                student.removeFromAssignedTeam(team);
                this.teamRepository.save(team);
            }

            for (Course course : student.getCourses()) {
                course.removeParticipant(student);
                student.removeFromAssignedCourse(course);
                this.courseRepository.save(course);
            }

            this.studentRepository.delete(student);
        });

        return "redirect:/students";
    }
}
