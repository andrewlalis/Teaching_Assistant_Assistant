package nl.andrewlalis.teaching_assistant_assistant.controllers.courses.entity.student_teams;

import nl.andrewlalis.teaching_assistant_assistant.model.Course;
import nl.andrewlalis.teaching_assistant_assistant.model.people.Student;
import nl.andrewlalis.teaching_assistant_assistant.model.people.teams.StudentTeam;
import nl.andrewlalis.teaching_assistant_assistant.model.people.teams.TeachingAssistantTeam;
import nl.andrewlalis.teaching_assistant_assistant.model.repositories.CourseRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Optional;

/**
 * Controller for exporting team information into readable files.
 */
@Controller
public class ExportStudentTeams {

    private CourseRepository courseRepository;

    protected ExportStudentTeams(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @GetMapping("/courses/{code}/student_teams/export")
    public void export(@PathVariable String code, HttpServletResponse response) throws IOException {
        Optional<Course> optionalCourse = this.courseRepository.findByCode(code);
        if (!optionalCourse.isPresent()) {
            response.sendError(404, "Course with code " + code + " not found");
            return;
        }

        Course course = optionalCourse.get();

        response.setContentType("text/*");

        OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream());
        writer.write("Student Teams Export for course: " + course.getName() + "\n");

        for (TeachingAssistantTeam teachingAssistantTeam : course.getTeachingAssistantTeams()) {
            writer.write("Teaching Assistant Team " + teachingAssistantTeam.getId() + ", Github team: " + teachingAssistantTeam.getGithubTeamName() + "\n");
            List<StudentTeam> assignedTeams = teachingAssistantTeam.getAssignedStudentTeams();
            for (StudentTeam studentTeam : assignedTeams) {
                writer.write("\tStudent Team " + studentTeam.getId() + ": ");
                for (Student student : studentTeam.getStudents()) {
                    writer.write(student.getFullName() + " (S" + student.getStudentNumber() + "), ");
                }
                writer.write("\n");
            }
        }

        response.flushBuffer();
    }
}
