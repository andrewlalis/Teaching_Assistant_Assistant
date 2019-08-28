package nl.andrewlalis.teaching_assistant_assistant.model.people.teams;

import nl.andrewlalis.teaching_assistant_assistant.model.BasicEntity;
import nl.andrewlalis.teaching_assistant_assistant.model.Course;
import nl.andrewlalis.teaching_assistant_assistant.model.people.Person;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A group consisting of one or more members that belongs to a {@link Course}.
 *
 * <p>
 *     Any Team contains a collection of {@link Person} objects, each representing a member of the team.
 * </p>
 */
@Entity
@Inheritance(
        strategy = InheritanceType.JOINED
)
public abstract class Team extends BasicEntity {

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
    protected List<Person> members;

    /**
     * The course that this team belongs to. A team cannot exist on its own, it must belong to a course.
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

    /**
     * Publicly available constructor in which a course is required.
     * @param course The course that this team is in.
     */
    public Team(Course course) {
        this();
        this.setCourse(course);
    }

    public void addMember(Person person) {
        if (!this.containsMember(person)) {
            this.members.add(person);
        }
    }

    public void addMembers(List<Person> people) {
        for (Person person : people) {
            this.addMember(person);
        }
    }

    public boolean containsMember(Person person) {
        return this.members.contains(person);
    }

    public void removeMember(Person person) {
        this.members.remove(person);
    }

    /*
    Getters and Setters
     */

    public void setCourse(Course course) {
        this.course = course;
    }

    /**
     * Gets a list of all members of this team.
     * @return A list of all the members in this team.
     */
    public List<Person> getMembers() {
        return this.members;
    }

    public Course getCourse() {
        return course;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Person p : this.getMembers()) {
            sb.append(p.getFullName()).append(", ");
        }
        return sb.toString();
    }
}
