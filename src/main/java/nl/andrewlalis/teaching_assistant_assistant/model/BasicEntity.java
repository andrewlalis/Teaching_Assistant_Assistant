package nl.andrewlalis.teaching_assistant_assistant.model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

/**
 * The basic entity properties which any entity in this system should have: an id and a creation timestamp. Every entity
 * in this system should extend from BasicEntity.
 */
@MappedSuperclass
public abstract class BasicEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    @Temporal(
            value = TemporalType.TIMESTAMP
    )
    @CreationTimestamp
    @Column
    private Date createdOn;

    protected BasicEntity() {}

    public Long getId() {
        return this.id;
    }

    public Date getCreatedOn() {
        return this.createdOn;
    }

    /**
     * Two entities are equal if they have the same id.
     * @param o The other object.
     * @return True if the entities are the same, or false if not.
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof BasicEntity) {
            BasicEntity other = (BasicEntity) o;
            return other.id.equals(this.id);
        }
        return false;
    }
}
