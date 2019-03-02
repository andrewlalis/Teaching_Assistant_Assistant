package nl.andrewlalis.teaching_assistant_assistant.model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

/**
 * The basic entity properties which any entity in this system should have: an id and a creation timestamp. Every entity
 * in this system should extend from BasicEntity.
 */
@MappedSuperclass
public class BasicEntity {

    @Id
    @GeneratedValue
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

}
