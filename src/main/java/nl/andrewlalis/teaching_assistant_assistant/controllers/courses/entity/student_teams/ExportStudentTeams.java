package nl.andrewlalis.teaching_assistant_assistant.controllers.courses.entity.student_teams;

import nl.andrewlalis.teaching_assistant_assistant.controllers.UserPageController;
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
import java.util.List;
import java.util.Optional;

/**
 * Controller for exporting team information into readable files.
 */
@Controller
public class ExportStudentTeams extends UserPageController {

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
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");

        response.getOutputStream().write(getStudentTeamsSummary(course));

        response.flushBuffer();
    }

    @GetMapping("/courses/{code}/student_teams/export_contact_info")
    public void exportContactInfo(@PathVariable String code, HttpServletResponse response) throws IOException {
        Optional<Course> optionalCourse = this.courseRepository.findByCode(code);
        if (!optionalCourse.isPresent()) {
            response.sendError(404, "Course with code " + code + " not found");
            return;
        }

        Course course = optionalCourse.get();
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");

        response.getOutputStream().write(getContactInfo(course));

        response.flushBuffer();
    }

    private byte[] getStudentTeamsSummary(Course course) {
        StringBuilder sb = new StringBuilder("Student Teams Export for Course: ");
        sb.append(course.getName()).append('\n');
        for (TeachingAssistantTeam teachingAssistantTeam : course.getTeachingAssistantTeams()) {
            sb.append("Teaching Assistant Team ").append(teachingAssistantTeam.getId()).append(", Github Team Name: ").append(teachingAssistantTeam.getGithubTeamName()).append('\n');
            List<StudentTeam> assignedTeams = teachingAssistantTeam.getAssignedStudentTeams();
            for (StudentTeam studentTeam : assignedTeams) {
                sb.append("\tStudent Team ").append(studentTeam.getId()).append(": ");
                for (Student student : studentTeam.getStudents()) {
                    sb.append(student.getFullName()).append(" (S").append(student.getStudentNumber()).append("), ");
                }
                sb.append('\n');
            }
        }
        return sb.toString().getBytes();
    }

    private byte[] getContactInfo(Course course) {
        StringBuilder sb = new StringBuilder("Student Team Contact Details\n");
        for (StudentTeam team : course.getStudentTeams()) {
            sb.append("2019_Team_").append(team.getId()).append(": ");
            for (Student student : team.getStudents()) {
                sb.append(student.getFullName()).append(" (").append(student.getEmailAddress()).append("), ");
            }
            sb.append("\n");
        }
        return sb.toString().getBytes();
    }
}
