package nl.andrewlalis.teaching_assistant_assistant.model.people.groups;

import nl.andrewlalis.teaching_assistant_assistant.model.BasicEntity;
import nl.andrewlalis.teaching_assistant_assistant.model.people.Person;

import javax.persistence.FetchType;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * A group consisting of one or more members. Child classes should define P as a sub class of Person to define custom
 * behavior if needed.
 * @param <P> The type of members this group contains.
 */
@MappedSuperclass
public abstract class Group<P extends Person> extends BasicEntity {

    /**
     * The list of members in this group.
     */
    @OneToMany(
            fetch = FetchType.EAGER,
            orphanRemoval = true
    )
    protected List<P> members;

    public void addMember(P person) {
        this.members.add(person);
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

    public List<P> getMembers() {
        return this.members;
    }

}
