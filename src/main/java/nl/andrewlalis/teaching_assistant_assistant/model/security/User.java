package nl.andrewlalis.teaching_assistant_assistant.model.security;

import nl.andrewlalis.teaching_assistant_assistant.model.BasicEntity;
import nl.andrewlalis.teaching_assistant_assistant.model.people.Person;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 * Represents a user of the website with some credentials.
 */
@Entity
public class User extends BasicEntity {

    /**
     * A unique username for the user.
     */
    @Column(nullable = false, unique = true)
    private String username;

    /**
     * The password for this user.
     */
    @Column
    private String password;

    @OneToOne
    @JoinColumn(
            name = "person_id",
            nullable = true,
            referencedColumnName = "id"
    )
    private Person person;

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public Person getPerson() {
        return this.person;
    }

}
