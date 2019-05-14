package nl.andrewlalis.teaching_assistant_assistant.controllers.courses.entity.teaching_assistant_teams;

import nl.andrewlalis.teaching_assistant_assistant.controllers.UserPageController;
import nl.andrewlalis.teaching_assistant_assistant.model.Course;
import nl.andrewlalis.teaching_assistant_assistant.model.people.teams.TeachingAssistantTeam;
import nl.andrewlalis.teaching_assistant_assistant.model.repositories.CourseRepository;
import nl.andrewlalis.teaching_assistant_assistant.model.repositories.TeachingAssistantTeamRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class TeachingAssistantTeamEntity extends UserPageController {

    private CourseRepository courseRepository;
    private TeachingAssistantTeamRepository teachingAssistantTeamRepository;

    protected TeachingAssistantTeamEntity(CourseRepository courseRepository, TeachingAssistantTeamRepository teachingAssistantTeamRepository) {
        this.courseRepository = courseRepository;
        this.teachingAssistantTeamRepository = teachingAssistantTeamRepository;
    }

    @GetMapping("/courses/{courseCode}/teaching_assistant_teams/{teamId}")
    public String get(@PathVariable String courseCode, @PathVariable long teamId, Model model) {
        Optional<Course> optionalCourse = this.courseRepository.findByCode(courseCode);
        Optional<TeachingAssistantTeam> optionalTeachingAssistantTeam = this.teachingAssistantTeamRepository.findById(teamId);

        if (optionalCourse.isPresent() && optionalTeachingAssistantTeam.isPresent()) {
            model.addAttribute("course", optionalCourse.get());
            model.addAttribute("teachingAssistantTeam", optionalTeachingAssistantTeam.get());
        }

        return "courses/entity/teaching_assistant_teams/entity";
    }

    @GetMapping("/courses/{courseCode}/teaching_assistant_teams/{teamId}/delete")
    public String delete(@PathVariable String courseCode, @PathVariable long teamId) {
        Optional<Course> optionalCourse = this.courseRepository.findByCode(courseCode);
        Optional<TeachingAssistantTeam> optionalTeachingAssistantTeam = this.teachingAssistantTeamRepository.findById(teamId);

        if (optionalCourse.isPresent() && optionalTeachingAssistantTeam.isPresent()) {
            Course course = optionalCourse.get();
            TeachingAssistantTeam team = optionalTeachingAssistantTeam.get();
            course.removeTeachingAssistantTeam(team);

            this.teachingAssistantTeamRepository.delete(team);
            this.courseRepository.save(course);
        }

        return "redirect:/courses/entity/teaching_assistants";
    }

}
