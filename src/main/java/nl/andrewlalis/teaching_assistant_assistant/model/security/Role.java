package nl.andrewlalis.teaching_assistant_assistant.model.security;

import nl.andrewlalis.teaching_assistant_assistant.model.BasicEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.List;

/**
 * Represents a role that a user has, which gives the user access to certain resources.
 */
@Entity
public class Role extends BasicEntity {

    /**
     * The name of this role.
     */
    @Column(nullable = false, unique = true)
    private String name;

    /**
     * The list of users with this role.
     */
    @ManyToMany(mappedBy = "roles")
    private List<User> users;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
