package nl.andrewlalis.teaching_assistant_assistant.services;

import nl.andrewlalis.teaching_assistant_assistant.model.Course;
import nl.andrewlalis.teaching_assistant_assistant.model.people.Student;
import nl.andrewlalis.teaching_assistant_assistant.model.people.teams.StudentTeam;
import nl.andrewlalis.teaching_assistant_assistant.model.people.teams.TeachingAssistantTeam;
import nl.andrewlalis.teaching_assistant_assistant.model.repositories.CourseRepository;
import nl.andrewlalis.teaching_assistant_assistant.model.repositories.StudentRepository;
import nl.andrewlalis.teaching_assistant_assistant.model.repositories.StudentTeamRepository;
import nl.andrewlalis.teaching_assistant_assistant.model.repositories.TeachingAssistantTeamRepository;
import nl.andrewlalis.teaching_assistant_assistant.util.github.GithubManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * This service is used to control the manipulation, creation and deletion of student teams.
 */
@Service
public class StudentTeamService {

    private Logger logger = LogManager.getLogger(StudentTeamService.class);

    private StudentTeamRepository studentTeamRepository;
    private StudentRepository studentRepository;
    private CourseRepository courseRepository;
    private TeachingAssistantTeamRepository teachingAssistantTeamRepository;

    public StudentTeamService(
            StudentTeamRepository studentTeamRepository,
            StudentRepository studentRepository,
            CourseRepository courseRepository,
            TeachingAssistantTeamRepository teachingAssistantTeamRepository
    ) {
        this.studentTeamRepository = studentTeamRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.teachingAssistantTeamRepository = teachingAssistantTeamRepository;
    }

    /**
     * Creates a new empty student team.
     * @param course The course that the student team is in.
     * @return The newly created student team.
     */
    public StudentTeam createNewStudentTeam(Course course) {
        StudentTeam newTeam = new StudentTeam(course);
        course.addStudentTeam(newTeam);
        this.courseRepository.save(course);
        logger.info("Created new team.");

        return newTeam;
    }

    /**
     * Creates a new student team, and populates it with some students and automatically assigns a teaching assistant
     * team.
     * @param course The course to which the team will belong.
     * @param students The list of students to add to the team.
     * @param teachingAssistantTeam The teaching assistant team responsible for this new team.
     * @return The newly created student team.
     */
    public StudentTeam createNewStudentTeam(Course course, List<Student> students, TeachingAssistantTeam teachingAssistantTeam) {
        StudentTeam emptyTeam = this.createNewStudentTeam(course);

        emptyTeam.setAssignedTeachingAssistantTeam(teachingAssistantTeam);
        teachingAssistantTeam.addAssignedStudentTeam(emptyTeam);
        this.teachingAssistantTeamRepository.save(teachingAssistantTeam);

        for (Student student : students) {
            this.addStudent(emptyTeam, student);
        }

        return emptyTeam;
    }

    /**
     * Adds a new student to this team.
     * @param team The team to add the student to.
     * @param student The student to add.
     */
    public void addStudent(StudentTeam team, Student student) {
        team.addMember(student);
        student.assignToTeam(team);
        this.studentTeamRepository.save(team);
        this.studentRepository.save(student);
    }

    /**
     * Removes a single student from a team, and if that team has a github repository, tries to remove the student from
     * that as well.
     * @param studentTeam The student team to remove the student from.
     * @param student The student to remove.
     */
    public void removeStudent(StudentTeam studentTeam, Student student) {
        studentTeam.removeMember(student);
        student.removeFromAssignedTeam(studentTeam);

        this.studentTeamRepository.save(studentTeam);
        this.studentRepository.save(student);

        if (studentTeam.getGithubRepositoryName() != null) {
            try {
                logger.debug("Removing " + student.getGithubUsername() + " from repository " + studentTeam.getGithubRepositoryName());
                GithubManager manager = new GithubManager(studentTeam.getCourse().getApiKey());
                manager.removeCollaborator(studentTeam, student);
            } catch (IOException e) {
                logger.catching(e);
                logger.error("Could not remove student from repository: " + studentTeam.getGithubRepositoryName());
            }
        }

        logger.info("Removed student " + student.getFullName() + " from team " + studentTeam.getId());
    }

    /**
     * Assigns a new teaching assistant team to a student team.
     * @param studentTeam The student team.
     * @param teachingAssistantTeam The teaching assistant team to assign the student team to.
     *                              This may be null.
     */
    public void assignTeachingAssistantTeam(StudentTeam studentTeam, TeachingAssistantTeam teachingAssistantTeam) {
        TeachingAssistantTeam oldTeachingAssistantTeam = studentTeam.getAssignedTeachingAssistantTeam();

        if (oldTeachingAssistantTeam != null) {
            oldTeachingAssistantTeam.removeAssignedStudentTeam(studentTeam);
            studentTeam.setAssignedTeachingAssistantTeam(null);
            this.teachingAssistantTeamRepository.save(oldTeachingAssistantTeam);
        }

        if (teachingAssistantTeam != null) {
            studentTeam.setAssignedTeachingAssistantTeam(teachingAssistantTeam);
            teachingAssistantTeam.addAssignedStudentTeam(studentTeam);
            this.teachingAssistantTeamRepository.save(teachingAssistantTeam);
        }

        this.studentTeamRepository.save(studentTeam);

        logger.info("Assigned teaching assistant team " + teachingAssistantTeam + " to student team " + studentTeam.getId());
    }

    /**
     * Uses a {@link GithubManager} to generate a repository for this student team.
     * @param team The team to generate a repository for.
     */
    public void generateRepository(StudentTeam team) {
        if (team.getGithubRepositoryName() == null) {
            try {
                GithubManager manager = new GithubManager(team.getCourse().getApiKey());
                String name = manager.generateStudentTeamRepository(team);
                team.setGithubRepositoryName(name);
                this.studentTeamRepository.save(team);
            } catch (IOException e) {
                logger.error("Could not generate repository.");
            }
        } else {
            logger.warn("Repository " + team.getGithubRepositoryName() + " already exists.");
        }
    }

    /**
     * Removes the given team, archives the repository if one exists.
     * @param team The team to remove.
     */
    public void removeTeam(StudentTeam team) {
        Course course = team.getCourse();

        // Remove the student team at all costs!
        if (team.getGithubRepositoryName() != null) {
            // First remove all student collaborators.
            try {
                GithubManager manager = new GithubManager(course.getApiKey());
                manager.deactivateRepository(team);
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("Could not deactivate repository.");
            }
        }

        // Remove all students from this team.
        for (Student s : team.getStudents()) {
            s.removeFromAssignedTeam(team);
            team.removeMember(s);
            this.studentRepository.save(s);

            logger.debug("Removed student " + s.getFullName() + " from team " + team.getId());
        }

        // Remove the TA team assignment.
        TeachingAssistantTeam teachingAssistantTeam = team.getAssignedTeachingAssistantTeam();
        if (teachingAssistantTeam != null) {
            teachingAssistantTeam.removeAssignedStudentTeam(team);
            this.teachingAssistantTeamRepository.save(teachingAssistantTeam);
            logger.debug("Removed team " + team.getId() + " from Teaching Assistant Team " + teachingAssistantTeam.getId() + " assigned teams list.");
        }

        team.setAssignedTeachingAssistantTeam(null);

        // Remove the repository from the course and delete it.
        course.removeStudentTeam(team);
        this.studentTeamRepository.delete(team);
        this.courseRepository.save(course);

        logger.info("Removed team " + team.getId());
    }

    /**
     * Merges all teams consisting of a single student so that new teams are generated.
     *
     * TODO: Make this team size independent.
     */
    public void mergeSingleTeams() {

    }

}
