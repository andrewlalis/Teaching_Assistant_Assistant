package nl.andrewlalis.teaching_assistant_assistant.controllers.courses.entity.student_teams;

import nl.andrewlalis.teaching_assistant_assistant.model.people.teams.StudentTeam;
import nl.andrewlalis.teaching_assistant_assistant.model.repositories.StudentTeamRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class StudentTeamEntity {

    private StudentTeamRepository studentTeamRepository;

    protected StudentTeamEntity(StudentTeamRepository studentTeamRepository) {
        this.studentTeamRepository = studentTeamRepository;
    }

    /**
     * Gets data for a specific student team.
     * @param courseCode The course code for the course in which the team resides.
     * @param teamId The id of the team.
     * @param model The view model.
     * @return The name of the template which will be used to view the student team.
     */
    @GetMapping("/courses/{courseCode}/student_teams/{teamId}")
    public String get(@PathVariable String courseCode, @PathVariable int teamId, Model model) {
        Optional<StudentTeam> optionalStudentTeam = this.studentTeamRepository.findByCourseCodeAndId(courseCode, teamId);
        optionalStudentTeam.ifPresent(team -> model.addAttribute("student_team", team));

        return "courses/entity/student_teams/entity";
    }
}
