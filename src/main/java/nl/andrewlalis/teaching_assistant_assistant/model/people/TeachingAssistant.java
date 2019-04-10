package nl.andrewlalis.teaching_assistant_assistant.model.people;

import javax.persistence.Entity;

/**
 * Represents a teaching assistant of a course, or in other words, a grader and administrator.
 */
@Entity
public class TeachingAssistant extends Person {

//    /**
//     * The list of all feedback given by a teaching assistant.
//     */
//    @OneToMany
//    @JoinColumn(name = "teaching_assistant_id")
//    private List<SectionGrade> sectionGrades;

    /**
     * Default constructor for JPA.
     */
    protected TeachingAssistant() {
//        this.sectionGrades = new ArrayList<>();
    }

    public TeachingAssistant(String firstName, String lastName, String emailAddress) {
        super(firstName, lastName, emailAddress);
    }

//    public List<SectionGrade> getSectionGrades() {
//        return this.sectionGrades;
//    }
//
//    public void setSectionGrades(List<SectionGrade> newSectionGrades) {
//        this.sectionGrades = newSectionGrades;
//    }
}
