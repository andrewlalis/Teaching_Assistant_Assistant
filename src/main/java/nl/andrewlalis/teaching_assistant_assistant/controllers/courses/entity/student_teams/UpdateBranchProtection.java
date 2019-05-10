package nl.andrewlalis.teaching_assistant_assistant.controllers.courses.entity.student_teams;

import nl.andrewlalis.teaching_assistant_assistant.model.Course;
import nl.andrewlalis.teaching_assistant_assistant.model.people.teams.StudentTeam;
import nl.andrewlalis.teaching_assistant_assistant.model.repositories.CourseRepository;
import nl.andrewlalis.teaching_assistant_assistant.util.github.GithubManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.util.Optional;

/**
 * Updates branch protection for all student repositories in a given course.
 */
@Controller
public class UpdateBranchProtection {

    private CourseRepository courseRepository;

    protected UpdateBranchProtection(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @GetMapping("/courses/{code}/student_teams/branch_protection_update")
    public String get(@PathVariable String code) {
        Optional<Course> optionalCourse = this.courseRepository.findByCode(code);
        optionalCourse.ifPresent(course -> {
            GithubManager manager;
            try {
                manager = new GithubManager(course.getApiKey());
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            for (StudentTeam team : course.getStudentTeams()) {
                try {
                    manager.updateBranchProtection(course.getGithubOrganizationName(), team.getGithubRepositoryName(), team.getAssignedTeachingAssistantTeam().getGithubTeamName());
                    System.out.println("Updated branch protection for repository " + team.getGithubRepositoryName());
                } catch (IOException e) {
                    e.printStackTrace();
                    System.err.println("Error occurred while trying to enable branch protection for repository " + team.getId());
                }
            }
        });

        return "redirect:/courses/{code}/student_teams";
    }
}
