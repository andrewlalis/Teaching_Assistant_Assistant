package nl.andrewlalis.teaching_assistant_assistant.controllers.courses.entity.student_teams;

import nl.andrewlalis.teaching_assistant_assistant.controllers.UserPageController;
import nl.andrewlalis.teaching_assistant_assistant.model.Course;
import nl.andrewlalis.teaching_assistant_assistant.model.people.teams.StudentTeam;
import nl.andrewlalis.teaching_assistant_assistant.model.repositories.CourseRepository;
import nl.andrewlalis.teaching_assistant_assistant.model.repositories.StudentTeamRepository;
import nl.andrewlalis.teaching_assistant_assistant.util.github.GithubManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.util.Optional;

@Controller
public class GenerateRepositories extends UserPageController {

    private CourseRepository courseRepository;
    private StudentTeamRepository studentTeamRepository;

    protected GenerateRepositories(CourseRepository courseRepository, StudentTeamRepository studentTeamRepository) {
        this.courseRepository = courseRepository;
        this.studentTeamRepository = studentTeamRepository;
    }

    @GetMapping("/courses/{courseCode}/student_teams/generate_repositories")
    public String get(@PathVariable String courseCode, Model model) {
        Optional<Course> optionalCourse = this.courseRepository.findByCode(courseCode);
        optionalCourse.ifPresent(course -> model.addAttribute("course", course));

        return "courses/entity/student_teams/generate_repositories";
    }

    @PostMapping(
            value = "/courses/{courseCode}/student_teams/generate_repositories",
            consumes = "application/x-www-form-urlencoded"
    )
    public String post(@PathVariable String courseCode) {
        System.out.println("Post received for " + courseCode);
        Optional<Course> optionalCourse = this.courseRepository.findByCode(courseCode);
        optionalCourse.ifPresent(course -> {
            GithubManager manager;

            try {
                manager = new GithubManager(course.getApiKey());
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            for (StudentTeam studentTeam : course.getStudentTeams()) {
                System.out.println("Generating repository for team " + studentTeam.getId());
                String repositoryName = manager.generateStudentTeamRepository(studentTeam);
                if (repositoryName == null) {
                    System.err.println("An error occurred while generating a repository for student team " + studentTeam.getId());
                    continue;
                }
                studentTeam.setGithubRepositoryName(repositoryName);
                this.studentTeamRepository.save(studentTeam);
                System.out.println("Done\n");
            }
        });

        return "redirect:/courses/{courseCode}";
    }
}
