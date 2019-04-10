package nl.andrewlalis.teaching_assistant_assistant.model.people;

import nl.andrewlalis.teaching_assistant_assistant.model.BasicEntity;
import nl.andrewlalis.teaching_assistant_assistant.model.people.teams.Team;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents any person (teaching assistant, student, or other) that exists in this application.
 */
@Entity
@Table(name = "people")
@Inheritance(
        strategy = InheritanceType.JOINED
)
public abstract class Person extends BasicEntity {

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column
    private String emailAddress;

    /**
     * The list of teams that this person belongs to.
     */
    @ManyToMany(
            fetch = FetchType.LAZY,
            mappedBy = "members"
    )
    private List<Team> teams;

    /**
     * Default constructor for JPA.
     */
    protected Person () {
        this.teams = new ArrayList<>();
    }

    /**
     * Constructs a new Person.
     * @param firstName The person's first name.
     * @param lastName The person's last name.
     * @param emailAddress The person's email address.
     */
    public Person(String firstName, String lastName, String emailAddress) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
    }

    public void assignToTeam(Team team) {
        this.teams.add(team);
    }

    /*
    Getters and Setters
     */

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    @Override
    public String toString() {
        return this.getFirstName() + ' ' + this.getLastName() + '[' + this.getId() + ']';
    }
}
