package nl.andrewlalis.teaching_assistant_assistant.model.people.teams;

import nl.andrewlalis.teaching_assistant_assistant.model.assignments.grades.SectionGrade;
import nl.andrewlalis.teaching_assistant_assistant.model.people.TeachingAssistant;
import nl.andrewlalis.teaching_assistant_assistant.model.people.TeachingAssistantRole;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * A group of teaching assistants.
 */
@Entity
public class TeachingAssistantTeam extends Team<TeachingAssistant> {

    /**
     * The role that this teaching assistant team plays.
     */
    @Column
    private TeachingAssistantRole role;

    @OneToMany
    @JoinColumn(
            name = "teaching_assistant_team_id"
    )
    private List<SectionGrade> sectionGrades;

    /**
     * Default constructor for JPA.
     */
    public TeachingAssistantTeam() {}

    @Override
    public void addMember(TeachingAssistant person) {
        this.getMembers().add(person);
    }
}
