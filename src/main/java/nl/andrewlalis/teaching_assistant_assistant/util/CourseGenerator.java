package nl.andrewlalis.teaching_assistant_assistant.util;

import nl.andrewlalis.teaching_assistant_assistant.model.Course;
import nl.andrewlalis.teaching_assistant_assistant.model.people.Student;
import nl.andrewlalis.teaching_assistant_assistant.model.people.TeachingAssistant;
import nl.andrewlalis.teaching_assistant_assistant.model.people.teams.StudentTeam;
import nl.andrewlalis.teaching_assistant_assistant.model.people.teams.TeachingAssistantTeam;
import nl.andrewlalis.teaching_assistant_assistant.model.repositories.CourseRepository;
import nl.andrewlalis.teaching_assistant_assistant.model.repositories.PersonRepository;
import nl.andrewlalis.teaching_assistant_assistant.model.repositories.TeamRepository;

import java.util.ArrayList;
import java.util.List;

public class CourseGenerator extends TestDataGenerator<Course> {

    private static final String[] COURSE_NAMES = {
            "Math",
            "Biology",
            "Psychology",
            "Liberal Arts",
            "English",
            "Spanish",
            "German",
            "Programming",
            "Physics",
            "Chemistry",
            "Geometry",
            "Calculus",
            "Algebra"
    };

    private int studentGroupCount = 50;
    private int studentGroupSize = 2;
    private int teachingAssistantGroupCount = 10;
    private int teachingAssistantGroupSize = 2;

    private CourseRepository courseRepository;
    private TeamRepository teamRepository;
    private PersonRepository personRepository;

    public CourseGenerator(CourseRepository courseRepository, TeamRepository teamRepository, PersonRepository personRepository) {
        super(0);

        this.courseRepository = courseRepository;
        this.teamRepository = teamRepository;
        this.personRepository = personRepository;
    }

    public CourseGenerator(long seed, int studentGroupSize, int teachingAssistantGroupSize, int studentGroupCount, int teachingAssistantGroupCount, CourseRepository courseRepository, TeamRepository teamRepository, PersonRepository personRepository) {
        super(seed);
        this.studentGroupSize = studentGroupSize;
        this.teachingAssistantGroupSize = teachingAssistantGroupSize;
        this.studentGroupCount = studentGroupCount;
        this.teachingAssistantGroupCount = teachingAssistantGroupCount;

        this.courseRepository = courseRepository;
        this.teamRepository = teamRepository;
        this.personRepository = personRepository;
    }

    @Override
    public Course generate() {
        Course course = new Course(this.getRandomObjectFromArray(COURSE_NAMES), Integer.toString(this.getRandom().nextInt(1000)));
        List<StudentTeam> studentTeams = this.generateStudentTeams();
        List<TeachingAssistantTeam> teachingAssistantTeams = this.generateTeachingAssistantTeams();
        for (StudentTeam team : studentTeams) {
            course.addStudentGroup(team);
            team.setCourse(course);
        }
        for (TeachingAssistantTeam team : teachingAssistantTeams) {
            course.addTeachingAssistantGroup(team);
            team.setCourse(course);
        }
        return course;
    }

    private List<StudentTeam> generateStudentTeams() {
        StudentGenerator studentGenerator = new StudentGenerator(this.getSeed());
        List<StudentTeam> teams = new ArrayList<>(this.studentGroupCount);
        for (int i = 0; i < this.studentGroupCount; i++) {
            StudentTeam team = new StudentTeam();
            List<Student> students = studentGenerator.generateList(this.studentGroupSize);
            for (Student s : students) {
                s.assignToTeam(team);
                team.addMember(s);
            }
            teams.add(team);
        }
        return teams;
    }

    private List<TeachingAssistantTeam> generateTeachingAssistantTeams() {
        TeachingAssistantGenerator teachingAssistantGenerator = new TeachingAssistantGenerator(this.getSeed() + 1);
        List<TeachingAssistantTeam> teams = new ArrayList<>(this.teachingAssistantGroupCount);
        for (int i = 0; i < this.teachingAssistantGroupCount; i++) {
            TeachingAssistantTeam team = new TeachingAssistantTeam();
            List<TeachingAssistant> teachingAssistants = teachingAssistantGenerator.generateList(this.teachingAssistantGroupSize);
            for (TeachingAssistant t : teachingAssistants) {
                t.assignToTeam(team);
                team.addMember(t);
            }
            teams.add(team);
        }
        return teams;
    }
}
