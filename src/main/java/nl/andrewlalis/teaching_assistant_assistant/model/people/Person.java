package nl.andrewlalis.teaching_assistant_assistant.model.people;

import nl.andrewlalis.teaching_assistant_assistant.model.BasicEntity;

import javax.persistence.*;

/**
 * Represents any person (teaching assistant, student, or other) that exists in this application.
 */
@Entity
@Inheritance(
        strategy = InheritanceType.JOINED
)
public class Person extends BasicEntity {

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column
    private String emailAddress;

    /**
     * Default constructor for JPA.
     */
    protected Person () {}

    /**
     * Constructs a new Person.
     * @param firstName The person's first name.
     * @param lastName The person's last name.
     * @param emailAddress The person's email address.
     */
    public Person(String firstName, String lastName, String emailAddress) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
    }

}
