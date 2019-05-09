package nl.andrewlalis.teaching_assistant_assistant.controllers.courses.entity.student_teams;

import nl.andrewlalis.teaching_assistant_assistant.model.Course;
import nl.andrewlalis.teaching_assistant_assistant.model.people.Student;
import nl.andrewlalis.teaching_assistant_assistant.model.people.teams.StudentTeam;
import nl.andrewlalis.teaching_assistant_assistant.model.people.teams.TeachingAssistantTeam;
import nl.andrewlalis.teaching_assistant_assistant.model.repositories.CourseRepository;
import nl.andrewlalis.teaching_assistant_assistant.model.repositories.StudentRepository;
import nl.andrewlalis.teaching_assistant_assistant.model.repositories.StudentTeamRepository;
import nl.andrewlalis.teaching_assistant_assistant.model.repositories.TeachingAssistantTeamRepository;
import nl.andrewlalis.teaching_assistant_assistant.util.github.GithubManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
public class StudentTeamEntity {

    private StudentTeamRepository studentTeamRepository;
    private CourseRepository courseRepository;
    private StudentRepository studentRepository;
    private TeachingAssistantTeamRepository teachingAssistantTeamRepository;

    protected StudentTeamEntity(
            StudentTeamRepository studentTeamRepository,
            CourseRepository courseRepository,
            StudentRepository studentRepository,
            TeachingAssistantTeamRepository teachingAssistantTeamRepository
    ) {
        this.studentTeamRepository = studentTeamRepository;
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
        this.teachingAssistantTeamRepository = teachingAssistantTeamRepository;
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

    @PostMapping("/courses/{courseCode}/student_teams/create")
    public String postCreate(@PathVariable String courseCode, Model model) {
        Optional<Course> optionalCourse = this.courseRepository.findByCode(courseCode);
        optionalCourse.ifPresent(course -> {
            StudentTeam team = new StudentTeam(course);
            course.addStudentTeam(team);
            this.courseRepository.save(course);
        });

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
            StudentTeam team = optionalStudentTeam.get();
            Student student = optionalStudent.get();

            team.addMember(student);
            student.assignToTeam(team);
            this.studentTeamRepository.save(team);
            this.studentRepository.save(student);
        }

        return "redirect:/courses/{courseCode}/student_teams";
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

            StudentTeam studentTeam = optionalStudentTeam.get();
            TeachingAssistantTeam oldTeam = studentTeam.getAssignedTeachingAssistantTeam();

            // Unset old TA team if it exists.
            if (oldTeam != null) {
                oldTeam.removeAssignedStudentTeam(studentTeam);
                studentTeam.setAssignedTeachingAssistantTeam(null);
                this.teachingAssistantTeamRepository.save(oldTeam);
                this.studentTeamRepository.save(studentTeam);
            }

            // Set new TA team if it exists.
            if (teachingAssistantTeam != null) {
                studentTeam.setAssignedTeachingAssistantTeam(teachingAssistantTeam);
                teachingAssistantTeam.addAssignedStudentTeam(studentTeam);
                this.teachingAssistantTeamRepository.save(teachingAssistantTeam);
                this.studentTeamRepository.save(studentTeam);
            }
        }

        return "redirect:/courses/{courseCode}/student_teams";
    }

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
            Student student = optionalStudent.get();
            StudentTeam team = optionalStudentTeam.get();
            Course course = optionalCourse.get();

            // If the team has a github repository, remove this student as a collaborator.
            if (team.getGithubRepositoryName() != null) {
                try {
                    GithubManager manager = new GithubManager(course.getApiKey());
                    manager.removeCollaborator(team, student);
                    System.out.println("Removed " + student.getGithubUsername() + " from " + team.getGithubRepositoryName());
                } catch (IOException e) {
                    System.err.println("Could not remove student from repository: " + team.getGithubRepositoryName());
                }
            }

            team.removeMember(student);
            student.removeFromAssignedTeam(team);

            this.studentTeamRepository.save(team);
            this.studentRepository.save(student);
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
            StudentTeam team = optionalStudentTeam.get();
            Course course = optionalCourse.get();

            if (team.getGithubRepositoryName() == null) {
                System.out.println("Generating repository.");
                try {
                    GithubManager manager = new GithubManager(course.getApiKey());
                    String name = manager.generateStudentTeamRepository(team);
                    team.setGithubRepositoryName(name);
                    this.studentTeamRepository.save(team);

                } catch (IOException e) {
                    System.err.println("Could not generate repository.");
                }
            } else {
                System.err.println("Repository already exists.");
            }

        } else {
            System.err.println("Could not find all objects.");
        }

        return "redirect:/courses/{courseCode}/student_teams/{teamId}";
    }

    @GetMapping("/courses/{courseCode}/student_teams/{teamId}/remove")
    public String remove(@PathVariable String courseCode, @PathVariable long teamId) {
        Optional<Course> optionalCourse = this.courseRepository.findByCode(courseCode);
        Optional<StudentTeam> optionalStudentTeam = this.studentTeamRepository.findById(teamId);

        if (optionalCourse.isPresent() && optionalStudentTeam.isPresent()) {
            StudentTeam team = optionalStudentTeam.get();
            Course course = optionalCourse.get();

            // Remove the student team at all costs!
            if (team.getGithubRepositoryName() != null) {
                // First remove all student collaborators.
                try {
                    GithubManager manager = new GithubManager(course.getApiKey());
                    manager.deactivateRepository(team);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.err.println("Could not deactivate repository.");
                }
            }

            // Remove all students from this team.
            for (Student s : team.getStudents()) {
                s.removeFromAssignedTeam(team);
                team.removeMember(s);
                this.studentRepository.save(s);
            }

            // Remove the TA team assignment.
            TeachingAssistantTeam teachingAssistantTeam = team.getAssignedTeachingAssistantTeam();
            teachingAssistantTeam.removeAssignedStudentTeam(team);
            team.setAssignedTeachingAssistantTeam(null);
            this.teachingAssistantTeamRepository.save(teachingAssistantTeam);

            // Remove the repository from the course and delete it.
            course.removeStudentTeam(team);
            this.studentTeamRepository.delete(team);
            this.courseRepository.save(course);
        }

        return "redirect:/courses/{courseCode}/student_teams";
    }
}
