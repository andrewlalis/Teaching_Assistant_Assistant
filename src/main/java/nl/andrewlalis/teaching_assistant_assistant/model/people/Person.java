package nl.andrewlalis.teaching_assistant_assistant.model.people;

import nl.andrewlalis.teaching_assistant_assistant.model.BasicEntity;
import nl.andrewlalis.teaching_assistant_assistant.model.Course;
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

    /**
     * The person's first name.
     */
    @Column(nullable = false)
    private String firstName;

    /**
     * The person's last name.
     */
    @Column(nullable = false)
    private String lastName;

    /**
     * The person's email address.
     */
    @Column
    private String emailAddress;

    /**
     * The person's github username.
     */
    @Column
    private String githubUsername;

    /**
     * The list of teams that this person belongs to. Because a person can belong to more than one team, it is implied
     * that each person exists in only one location in the database. Therefore, if one person is enrolled in two courses
     * both of which use this application, only one instance of that person will exist, and will simply be related to
     * two teams.
     */
    @ManyToMany(
            fetch = FetchType.LAZY,
            mappedBy = "members"
    )
    private List<Team> teams;

    /**
     * The list of courses that this person belongs to.
     */
    @ManyToMany(
            fetch = FetchType.LAZY,
            mappedBy = "participants"
    )
    private List<Course> courses;

    /**
     * Default constructor for JPA.
     */
    protected Person () {
        this.teams = new ArrayList<>();
        this.courses = new ArrayList<>();
    }

    /**
     * Constructs a new Person.
     * @param firstName The person's first name.
     * @param lastName The person's last name.
     * @param emailAddress The person's email address.
     * @param githubUsername The person's github username;
     */
    public Person(String firstName, String lastName, String emailAddress, String githubUsername) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.githubUsername = githubUsername;
    }

    /**
     * Assigns this person to a team.
     * @param team The team to assign to.
     */
    public void assignToTeam(Team team) {
        if (!this.teams.contains(team)) {
            this.teams.add(team);
        }
    }

    /**
     * Removes this person from a team.
     * @param team The team to remove from.
     */
    public void removeFromAssignedTeam(Team team) {
        this.teams.remove(team);
    }

    public void assignToCourse(Course course) {
        if (!this.courses.contains(course)) {
            this.courses.add(course);
        }
    }

    public void removeFromAssignedCourse(Course course) {
        this.courses.remove(course);
    }

    /*
    Getters and Setters
     */

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return this.getFirstName() + ' ' + this.getLastName();
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getGithubUsername() {
        return this.githubUsername;
    }

    public void setGithubUsername(String githubUsername) {
        this.githubUsername = githubUsername;
    }

    public List<Course> getCourses() {
        return this.courses;
    }

    public List<Team> getTeams() {
        return this.teams;
    }

    /**
     * Determines if two Persons are equal. They are considered equal when all of the basic identifying information
     * about the person is the same, regardless of case.
     * @param o The other object.
     * @return True if the other object is the same person, or false if not.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (o instanceof Person) {
            Person p = (Person) o;
            return (
                    this.getFirstName().equalsIgnoreCase(p.getFirstName())
                    && this.getLastName().equalsIgnoreCase(p.getLastName())
                    && this.getEmailAddress().equalsIgnoreCase(p.getEmailAddress())
                    && this.getGithubUsername().equalsIgnoreCase(p.getGithubUsername())
            );
        }

        return false;
    }

    @Override
    public String toString() {
        return "First Name: " + this.getFirstName()
                + ", Last Name: " + this.getLastName()
                + ", Email: " + this.getEmailAddress()
                + ", Github Username: " + this.getGithubUsername();
    }
}
