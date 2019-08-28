package nl.andrewlalis.teaching_assistant_assistant.controllers.courses.entity.teaching_assistant_teams;

import nl.andrewlalis.teaching_assistant_assistant.controllers.UserPageController;
import nl.andrewlalis.teaching_assistant_assistant.model.Course;
import nl.andrewlalis.teaching_assistant_assistant.model.people.teams.StudentTeam;
import nl.andrewlalis.teaching_assistant_assistant.model.people.teams.TeachingAssistantTeam;
import nl.andrewlalis.teaching_assistant_assistant.model.repositories.CourseRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
public class AssignToStudentTeams extends UserPageController {

    private CourseRepository courseRepository;

    protected AssignToStudentTeams(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @GetMapping("/courses/{code}/teaching_assistant_teams/assign_to_student_teams")
    public String get(@PathVariable String code, Model model) {
        Optional<Course> optionalCourse = this.courseRepository.findByCode(code);
        optionalCourse.ifPresent(course -> model.addAttribute("course", course));

        return "courses/entity/teaching_assistant_teams/assign_to_student_teams";
    }

    /**
     * Randomly assigns a teaching assistant team to each student team in such a way that all teaching assistant teams
     * should receive an equal number of student teams.
     * @param code The code for the course in which to perform this action.
     * @param seed A seed to use to determine randomness.
     * @return The view for the list of student teams in this course, to see the results of the action.
     */
    @PostMapping("/courses/{code}/teaching_assistant_teams/assign_to_student_teams")
    public String post(@PathVariable String code, @RequestParam(value = "seed") int seed) {
        Optional<Course> optionalCourse = this.courseRepository.findByCode(code);
        optionalCourse.ifPresent(course -> {
            List<StudentTeam> studentTeams = course.getStudentTeams();

            LinkedList<Integer> studentTeamQueue = new LinkedList<>();
            for (int i = 0; i < studentTeams.size(); i++) {
                studentTeamQueue.add(i);
            }
            Collections.shuffle(studentTeamQueue, new Random(seed));

            while (!studentTeamQueue.isEmpty()) {
                for (TeachingAssistantTeam taTeam : course.getTeachingAssistantTeams()) {
                    if (studentTeamQueue.isEmpty()) {
                        break;
                    }

                    StudentTeam studentTeam = studentTeams.get(studentTeamQueue.removeFirst());
                    studentTeam.setAssignedTeachingAssistantTeam(taTeam);
                    taTeam.addAssignedStudentTeam(studentTeam);
                }
            }

            this.courseRepository.save(course);
        });

        return "redirect:/courses/{code}/student_teams";
    }

}
