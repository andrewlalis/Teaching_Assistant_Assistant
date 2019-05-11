package nl.andrewlalis.teaching_assistant_assistant.model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

/**
 * An abstract object from which all persistent objects in this application should extend.
 * <p>
 *     The basic entity properties which any entity in this system should have: an {@code id} and a creation timestamp.
 *     Every entity in this system should extend from BasicEntity.
 * </p>
 *
 * <p>
 *     Every single entity in this system therefore is identified uniquely by a Long primary key, although it may also
 *     be identified by other codes or attributes defined in such an entity. For example, the {@link Course} object has
 *     a unique code, which can also be used to select a course.
 * </p>
 *
 */
@MappedSuperclass
public abstract class BasicEntity {

    /**
     * The primary key for any basic entity.
     */
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    /**
     * The date at which this basic entity was created.
     */
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
