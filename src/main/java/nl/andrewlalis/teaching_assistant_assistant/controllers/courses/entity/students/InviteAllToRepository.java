package nl.andrewlalis.teaching_assistant_assistant.controllers.courses.entity.students;

import nl.andrewlalis.teaching_assistant_assistant.model.Course;
import nl.andrewlalis.teaching_assistant_assistant.model.people.Student;
import nl.andrewlalis.teaching_assistant_assistant.model.repositories.CourseRepository;
import nl.andrewlalis.teaching_assistant_assistant.util.github.GithubManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class InviteAllToRepository {

    private CourseRepository courseRepository;

    protected InviteAllToRepository(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @GetMapping("/courses/{code}/students/invite_all")
    public String get(@PathVariable String code, Model model) {
        Optional<Course> optionalCourse =this.courseRepository.findByCode(code);
        optionalCourse.ifPresent(course -> model.addAttribute("course", course));

        return "courses/entity/students/invite_all";
    }

    @PostMapping("/courses/{code}/students/invite_all")
    public String post(
            @PathVariable String code,
            @RequestParam(value = "repository_name") String repositoryName,
            @RequestParam(value = "api_keys") String apiKeys,
            @RequestParam(value = "usernames", required = false) String usernames
    ) {
        Optional<Course> optionalCourse = this.courseRepository.findByCode(code);
        optionalCourse.ifPresent(course -> {

            // Get a list of all the github usernames to invite.
            List<String> githubUsernames = new ArrayList<>();
            if (usernames != null && !usernames.isEmpty()) {
                String[] usernamesRaw = usernames.split("\n");
                for (String username : usernamesRaw) {
                    githubUsernames.add(username.trim());
                }
            } else {
                for (Student student : course.getStudents()) {
                    githubUsernames.add(student.getGithubUsername());
                }
            }

            // Get a list of all our available keys.
            String[] rawKeys = apiKeys.split("\n");
            List<String> keys = new ArrayList<>();
            for (String rawKey : rawKeys) {
                keys.add(rawKey.trim());
            }

            String fullRepositoryName = course.getGithubOrganizationName() + '/' + repositoryName;

            int inviteCounter = 0;
            GithubManager manager;
            try {
                manager = new GithubManager(keys.remove(0));
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            List<String> failedNames = new ArrayList<>();

            while (!githubUsernames.isEmpty()) {
                if (inviteCounter == 50) {
                    System.out.println("Used up 50 invites on key.");
                    try {
                        manager = new GithubManager(keys.remove(0));
                        inviteCounter = 0;
                    } catch (IOException e) {
                        e.printStackTrace();
                        failedNames.addAll(githubUsernames);
                        return;
                    }
                }

                String username = githubUsernames.remove(0);
                try {
                    manager.addCollaborator(fullRepositoryName, username, "pull");
                    inviteCounter++;
                    System.out.println("\tInvited " + username);
                } catch (IOException e) {
                    //e.printStackTrace();
                    System.err.println("Could not add " + username + " to repository " + fullRepositoryName);
                    failedNames.add(username);
                }
            }

            System.err.println("The following github usernames have not been added.");
            for (String username : failedNames) {
                System.out.println("\t" + username);
            }
        });

        return "redirect:/courses/{code}/students";
    }
}
