package nl.andrewlalis.teaching_assistant_assistant.controllers.courses.entity.student_teams;

import nl.andrewlalis.teaching_assistant_assistant.controllers.UserPageController;
import nl.andrewlalis.teaching_assistant_assistant.model.Course;
import nl.andrewlalis.teaching_assistant_assistant.model.people.Student;
import nl.andrewlalis.teaching_assistant_assistant.model.people.teams.StudentTeam;
import nl.andrewlalis.teaching_assistant_assistant.model.people.teams.TeachingAssistantTeam;
import nl.andrewlalis.teaching_assistant_assistant.model.repositories.CourseRepository;
import nl.andrewlalis.teaching_assistant_assistant.model.repositories.StudentRepository;
import nl.andrewlalis.teaching_assistant_assistant.model.repositories.StudentTeamRepository;
import nl.andrewlalis.teaching_assistant_assistant.model.repositories.TeachingAssistantTeamRepository;
import nl.andrewlalis.teaching_assistant_assistant.services.StudentTeamService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class StudentTeamEntity extends UserPageController {

    private StudentTeamRepository studentTeamRepository;
    private CourseRepository courseRepository;
    private StudentRepository studentRepository;
    private TeachingAssistantTeamRepository teachingAssistantTeamRepository;
    private StudentTeamService studentTeamService;

    protected StudentTeamEntity(
            StudentTeamRepository studentTeamRepository,
            CourseRepository courseRepository,
            StudentRepository studentRepository,
            TeachingAssistantTeamRepository teachingAssistantTeamRepository,
            StudentTeamService studentTeamService
    ) {
        this.studentTeamRepository = studentTeamRepository;
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
        this.teachingAssistantTeamRepository = teachingAssistantTeamRepository;
        this.studentTeamService = studentTeamService;
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

    @GetMapping("/courses/{courseCode}/student_teams/create")
    public String getCreate(@PathVariable String courseCode, Model model) {
        Optional<Course> optionalCourse = this.courseRepository.findByCode(courseCode);
        optionalCourse.ifPresent(course -> model.addAttribute("course", course));

        return "courses/entity/student_teams/create";
    }

    /**
     * Mapping for creating a new student team.
     * @param courseCode The course code.
     * @param model The view model.
     * @return A redirect to the list of student teams.
     */
    @PostMapping("/courses/{courseCode}/student_teams/create")
    public String postCreate(@PathVariable String courseCode, Model model) {
        Optional<Course> optionalCourse = this.courseRepository.findByCode(courseCode);
        optionalCourse.ifPresent(course -> this.studentTeamService.createNewStudentTeam(course));

        return "redirect:/courses/{courseCode}/student_teams";
    }

    @GetMapping("/courses/{courseCode}/student_teams/{teamId}/add_student")
    public String getAddStudent(@PathVariable String courseCode, @PathVariable long teamId, Model model) {
        Optional<Course> optionalCourse = this.courseRepository.findByCode(courseCode);
        Optional<StudentTeam> optionalStudentTeam = this.studentTeamRepository.findById(teamId);

        if (optionalCourse.isPresent() && optionalStudentTeam.isPresent()) {
            List<Student> eligibleStudents = optionalCourse.get().getStudents();
            eligibleStudents.sort((s1, s2) -> s1.getLastName().compareToIgnoreCase(s2.getLastName()));
            model.addAttribute("course", optionalCourse.get());
            model.addAttribute("student_team", optionalStudentTeam.get());
            model.addAttribute("eligible_students", eligibleStudents);
        }

        return "courses/entity/student_teams/entity/add_student";
    }

    /**
     * Mapping for adding a new student to this team.
     * @param courseCode The course code.
     * @param teamId The id of the team to add the student to.
     * @param studentId The id of an existing student to add to this team.
     * @return A redirect to the list of student teams.
     */
    @PostMapping("/courses/{courseCode}/student_teams/{teamId}/add_student")
    public String postAddStudent(
            @PathVariable String courseCode,
            @PathVariable long teamId,
            @RequestParam(value = "student_id") long studentId
    ) {
        Optional<Course> optionalCourse = this.courseRepository.findByCode(courseCode);
        Optional<StudentTeam> optionalStudentTeam = this.studentTeamRepository.findById(teamId);
        Optional<Student> optionalStudent = this.studentRepository.findById(studentId);

        if (optionalCourse.isPresent() && optionalStudentTeam.isPresent() && optionalStudent.isPresent()) {
            this.studentTeamService.addStudent(optionalStudentTeam.get(), optionalStudent.get());
        }

        return "redirect:/courses/{courseCode}/student_teams/{teamId}";
    }

    @GetMapping("/courses/{courseCode}/student_teams/{teamId}/assign_teaching_assistant_team")
    public String getAssignTeachingAssistantTeam(
            @PathVariable String courseCode,
            @PathVariable long teamId,
            Model model
    ) {
        Optional<Course> optionalCourse = this.courseRepository.findByCode(courseCode);
        Optional<StudentTeam> optionalStudentTeam = this.studentTeamRepository.findById(teamId);

        if (optionalCourse.isPresent() && optionalStudentTeam.isPresent()) {
            model.addAttribute("course", optionalCourse.get());
            model.addAttribute("student_team", optionalStudentTeam.get());
        }

        return "courses/entity/student_teams/entity/assign_teaching_assistant_team";
    }

    /**
     * Endpoint for assigning a teaching assistant team to this student team.
     * @param courseCode The course code.
     * @param teamId The id of the student team.
     * @param teachingAssistantTeamId The id of the teaching assistant team.
     * @return A redirect to the team responsible.
     */
    @PostMapping("/courses/{courseCode}/student_teams/{teamId}/assign_teaching_assistant_team")
    public String postAssignTeachingAssistantTeam(
            @PathVariable String courseCode,
            @PathVariable long teamId,
            @RequestParam(value = "teaching_assistant_team_id") long teachingAssistantTeamId
    ) {
        Optional<Course> optionalCourse = this.courseRepository.findByCode(courseCode);
        Optional<StudentTeam> optionalStudentTeam = this.studentTeamRepository.findById(teamId);
        Optional<TeachingAssistantTeam> optionalTeachingAssistantTeam = this.teachingAssistantTeamRepository.findById(teachingAssistantTeamId);

        if (optionalCourse.isPresent() && optionalStudentTeam.isPresent()) {
            TeachingAssistantTeam teachingAssistantTeam = null;
            if (optionalTeachingAssistantTeam.isPresent()) {
                teachingAssistantTeam = optionalTeachingAssistantTeam.get();
            }

            this.studentTeamService.assignTeachingAssistantTeam(optionalStudentTeam.get(), teachingAssistantTeam);
        }

        return "redirect:/courses/{courseCode}/student_teams/{teamId}";
    }

    /**
     * Endpoint for removing a student from the student team.
     * @param courseCode The code for the course.
     * @param teamId The id of the team.
     * @param studentId The student's id.
     * @return A redirect to the team after the student is removed.
     */
    @GetMapping("/courses/{courseCode}/student_teams/{teamId}/remove_student/{studentId}")
    public String getRemoveStudent(
            @PathVariable String courseCode,
            @PathVariable long teamId,
            @PathVariable long studentId
    ) {
        Optional<Course> optionalCourse = this.courseRepository.findByCode(courseCode);
        Optional<StudentTeam> optionalStudentTeam = this.studentTeamRepository.findById(teamId);
        Optional<Student> optionalStudent = this.studentRepository.findById(studentId);

        if (optionalCourse.isPresent() && optionalStudentTeam.isPresent() && optionalStudent.isPresent()) {
            this.studentTeamService.removeStudent(optionalStudentTeam.get(), optionalStudent.get());
        }

        return "redirect:/courses/{courseCode}/student_teams/{teamId}";
    }

    @GetMapping("/courses/{courseCode}/student_teams/{teamId}/generate_repository")
    public String getGenerateRepository(
            @PathVariable String courseCode,
            @PathVariable long teamId
    ) {
        Optional<Course> optionalCourse = this.courseRepository.findByCode(courseCode);
        Optional<StudentTeam> optionalStudentTeam = this.studentTeamRepository.findById(teamId);

        if (optionalCourse.isPresent() && optionalStudentTeam.isPresent()) {
            this.studentTeamService.generateRepository(optionalStudentTeam.get());
        }

        return "redirect:/courses/{courseCode}/student_teams/{teamId}";
    }

    @GetMapping("/courses/{courseCode}/student_teams/{teamId}/remove")
    public String remove(@PathVariable String courseCode, @PathVariable long teamId) {
        Optional<Course> optionalCourse = this.courseRepository.findByCode(courseCode);
        Optional<StudentTeam> optionalStudentTeam = this.studentTeamRepository.findById(teamId);

        if (optionalCourse.isPresent() && optionalStudentTeam.isPresent()) {
            this.studentTeamService.removeTeam(optionalStudentTeam.get());
        }

        return "redirect:/courses/{courseCode}/student_teams";
    }
}
