package nl.andrewlalis.teaching_assistant_assistant.model.assignments.grades;

import nl.andrewlalis.teaching_assistant_assistant.model.BasicEntity;
import nl.andrewlalis.teaching_assistant_assistant.model.people.teams.TeachingAssistantTeam;

import javax.persistence.*;

/**
 * A grade for a particular section of a student's submission. This is the basic unit which makes up a grade.
 */
@Entity
public class SectionGrade extends BasicEntity {

    /**
     * The teaching assistant team responsible for giving this grade.
     */
    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    private TeachingAssistantTeam teachingAssistantTeam;

    /**
     * The overall grade to which this section grade belongs.
     */
    @ManyToOne(
            fetch = FetchType.EAGER,
            optional = false
    )
    private AssignmentGrade assignmentGrade;

    /**
     * The feedback object for this particular section grade.
     */
    @OneToOne(
            optional = false
    )
    private Feedback feedback;

    /**
     * The grade for this section.
     */
    @Column
    private float grade;

    /**
     * Default constructor for JPA.
     */
    protected SectionGrade() {}

    public SectionGrade(float grade, Feedback feedback, TeachingAssistantTeam teachingAssistantTeam, AssignmentGrade assignmentGrade) {
        this.grade = grade;
        this.feedback = feedback;
        this.teachingAssistantTeam = teachingAssistantTeam;
        this.assignmentGrade = assignmentGrade;
    }

    public float getGrade() {
        return grade;
    }
}
