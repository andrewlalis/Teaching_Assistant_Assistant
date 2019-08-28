package nl.andrewlalis.teaching_assistant_assistant.controllers.courses.entity.student_teams;

import nl.andrewlalis.teaching_assistant_assistant.controllers.UserPageController;
import nl.andrewlalis.teaching_assistant_assistant.model.Course;
import nl.andrewlalis.teaching_assistant_assistant.model.people.teams.StudentTeam;
import nl.andrewlalis.teaching_assistant_assistant.model.repositories.CourseRepository;
import nl.andrewlalis.teaching_assistant_assistant.model.repositories.StudentTeamRepository;
import nl.andrewlalis.teaching_assistant_assistant.services.StudentTeamService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Controller for the action to merge all single teams in a course.
 * TODO: Implement this functionality automatically.
 */
@Controller
public class MergeSingleTeams extends UserPageController {

    private Logger logger = LogManager.getLogger(MergeSingleTeams.class);

    private CourseRepository courseRepository;
    private StudentTeamRepository studentTeamRepository;

    private StudentTeamService studentTeamService;

    protected MergeSingleTeams(CourseRepository courseRepository, StudentTeamRepository studentTeamRepository, StudentTeamService studentTeamService) {
        this.courseRepository = courseRepository;
        this.studentTeamRepository = studentTeamRepository;
        this.studentTeamService = studentTeamService;
    }

    @GetMapping("/courses/{code}/student_teams/merge_single_teams")
    public String get(@PathVariable String code) {
        Optional<Course> optionalCourse = this.courseRepository.findByCode(code);
        if (optionalCourse.isPresent()) {
            Course course = optionalCourse.get();
            List<StudentTeam> singleTeams = this.getAllSingleTeams(course);
            singleTeams.forEach(team -> logger.info("Team " + team.getId() + " is a single team."));

//            while (singleTeams.size() > 1) {
//                StudentTeam single1 = singleTeams.remove(0);
//                StudentTeam single2 = singleTeams.remove(0);
//
//                // Todo: use a service here and when removing a team in another location to avoid duplication.
//                StudentTeam newTeam = this.studentTeamService.createNewStudentTeam(course);
//            }
        }

        return "redirect:/courses/{code}/student_teams";
    }

    private List<StudentTeam> getAllSingleTeams(Course course) {
        List<StudentTeam> allTeams = course.getStudentTeams();
        List<StudentTeam> singleTeams = new ArrayList<>();
        for (StudentTeam team : allTeams) {
            if (team.getMembers().size() == 1) {
                singleTeams.add(team);
            }
        }
        return singleTeams;
    }

}
