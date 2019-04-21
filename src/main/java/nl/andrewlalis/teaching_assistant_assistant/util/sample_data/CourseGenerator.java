package nl.andrewlalis.teaching_assistant_assistant.util.sample_data;

import nl.andrewlalis.teaching_assistant_assistant.model.Course;
import nl.andrewlalis.teaching_assistant_assistant.model.people.Student;
import nl.andrewlalis.teaching_assistant_assistant.model.people.TeachingAssistant;
import nl.andrewlalis.teaching_assistant_assistant.model.people.teams.StudentTeam;
import nl.andrewlalis.teaching_assistant_assistant.model.people.teams.TeachingAssistantTeam;

import java.util.ArrayList;
import java.util.List;

/**
 * Generator for a full course with TA and student teams, and all other course information.
 */
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

    public CourseGenerator() {
        super(0);
    }

    public CourseGenerator(long seed, int studentGroupSize, int teachingAssistantGroupSize, int studentGroupCount, int teachingAssistantGroupCount) {
        super(seed);
        this.studentGroupSize = studentGroupSize;
        this.teachingAssistantGroupSize = teachingAssistantGroupSize;
        this.studentGroupCount = studentGroupCount;
        this.teachingAssistantGroupCount = teachingAssistantGroupCount;
    }

    @Override
    public Course generate() {
        Course course = new Course(
                this.getRandomObjectFromArray(COURSE_NAMES) + this.getRandomInteger(0, 999),
                Integer.toString(this.getRandomInteger(0, 1000000))
        );
        List<StudentTeam> studentTeams = this.generateStudentTeams();
        List<TeachingAssistantTeam> teachingAssistantTeams = this.generateTeachingAssistantTeams();
        for (StudentTeam team : studentTeams) {
            course.addStudentTeam(team);
            team.setCourse(course);
        }
        for (TeachingAssistantTeam team : teachingAssistantTeams) {
            course.addTeachingAssistantTeam(team);
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
