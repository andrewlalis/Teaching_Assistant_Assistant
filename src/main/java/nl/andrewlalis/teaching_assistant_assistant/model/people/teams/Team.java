package nl.andrewlalis.teaching_assistant_assistant.model.people.teams;

import nl.andrewlalis.teaching_assistant_assistant.model.BasicEntity;
import nl.andrewlalis.teaching_assistant_assistant.model.Course;
import nl.andrewlalis.teaching_assistant_assistant.model.people.Person;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A group consisting of one or more members. Child classes should define P as a sub class of Person to define custom
 * behavior if needed.
 * @param <P> The type of members this group contains.
 */
@Entity
@Inheritance(
        strategy = InheritanceType.JOINED
)
public abstract class Team<P extends Person> extends BasicEntity {

    /**
     * The list of members in this group.
     */
    @ManyToMany(
            cascade = CascadeType.ALL
    )
    @JoinTable(
            name = "team_members",
            joinColumns = @JoinColumn(name = "team_id"),
            inverseJoinColumns = @JoinColumn(name = "person_id")
    )
    protected List<P> members;

    /**
     * The course that this team belongs to.
     */
    @ManyToOne(
            optional = false
    )
    private Course course;

    /**
     * Default constructor for JPA and initializing list of members for any child classes.
     */
    protected Team() {
        this.members = new ArrayList<>();
    }

    public void addMember(P person) {
        if (!this.containsMember(person)) {
            this.members.add(person);
        }
    }

    public void addMembers(List<P> people) {
        for (P person : people) {
            this.addMember(person);
        }
    }

    public boolean containsMember(P person) {
        return this.members.contains(person);
    }

    public void removeMember(P person) {
        this.members.remove(person);
    }

    /*
    Getters and Setters
     */

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<P> getMembers() {
        return this.members;
    }

    public Course getCourse() {
        return course;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (P p : this.getMembers()) {
            sb.append(p.toString()).append(", ");
        }
        return sb.toString();
    }
}
