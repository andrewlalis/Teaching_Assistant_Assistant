package nl.andrewlalis.teaching_assistant_assistant.controllers.courses.entity.teaching_assistant_teams;

import nl.andrewlalis.teaching_assistant_assistant.controllers.UserPageController;
import nl.andrewlalis.teaching_assistant_assistant.model.Course;
import nl.andrewlalis.teaching_assistant_assistant.model.people.TeachingAssistant;
import nl.andrewlalis.teaching_assistant_assistant.model.people.teams.TeachingAssistantTeam;
import nl.andrewlalis.teaching_assistant_assistant.model.repositories.CourseRepository;
import nl.andrewlalis.teaching_assistant_assistant.model.repositories.TeachingAssistantRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class CreateTeachingAssistantTeam extends UserPageController {

    private CourseRepository courseRepository;
    private TeachingAssistantRepository teachingAssistantRepository;

    protected CreateTeachingAssistantTeam(CourseRepository courseRepository, TeachingAssistantRepository teachingAssistantRepository) {
        this.courseRepository = courseRepository;
        this.teachingAssistantRepository = teachingAssistantRepository;
    }

    @GetMapping("/courses/{code}/teaching_assistant_teams/create")
    public String get(@PathVariable String code, Model model) {
        Optional<Course> optionalCourse = this.courseRepository.findByCode(code);
        optionalCourse.ifPresent(course -> model.addAttribute("course", course));
        return "courses/entity/teaching_assistant_teams/create";
    }

    @PostMapping(
            value = "/courses/{code}/teaching_assistant_teams",
            consumes = "application/x-www-form-urlencoded"
    )
    public String post(
            @PathVariable String code,
            @RequestParam(value = "github_team_name") String githubTeamName,
            @RequestParam(value = "id_1") long id1,
            Model model
    ) {
        TeachingAssistantTeam team = new TeachingAssistantTeam();
        team.setGithubTeamName(githubTeamName);

        Optional<Course> optionalCourse = this.courseRepository.findByCode(code);
        Optional<TeachingAssistant> optionalTeachingAssistant1 = this.teachingAssistantRepository.findById(id1);

        if (optionalCourse.isPresent() && optionalTeachingAssistant1.isPresent()) {
            Course course = optionalCourse.get();
            team.setCourse(course);

            team.addMember(optionalTeachingAssistant1.get());

            course.addTeachingAssistantTeam(team);
            this.courseRepository.save(course);

            model.addAttribute("course", course);

            return "courses/entity/teaching_assistant_teams";
        } else {
            System.out.println("Missing data!");
        }

        return "redirect:/courses/entity";
    }
}
