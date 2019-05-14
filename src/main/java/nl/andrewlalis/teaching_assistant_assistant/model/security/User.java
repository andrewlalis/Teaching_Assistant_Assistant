package nl.andrewlalis.teaching_assistant_assistant.model.security;

import nl.andrewlalis.teaching_assistant_assistant.model.BasicEntity;
import nl.andrewlalis.teaching_assistant_assistant.model.people.Person;

import javax.persistence.*;
import java.util.List;

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
    @Column(nullable = false)
    private String password;

    /**
     * Whether or not this user has been activated.
     */
    @Column(nullable = false)
    private boolean activated = false;

    /**
     * Whether or not this user has been locked (no more access).
     */
    @Column(nullable = false)
    private boolean locked = false;

    @ManyToMany(
            fetch = FetchType.EAGER
    )
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private List<Role> roles;

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

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
