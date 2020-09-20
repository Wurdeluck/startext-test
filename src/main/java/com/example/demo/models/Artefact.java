package com.example.demo.models;
// This import helps create tables in database

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

import static javax.persistence.TemporalType.TIMESTAMP;

// The Artefact class is annotated with @Entity, indicating that it is a JPA entity.
// @Table annotation exists, this entity is mapped to a table named artefacts.
@Entity
@Table(name = "artefacts")
@EntityListeners(AuditingEntityListener.class)
//public class Artefact extends Auditable<String> {
public class Artefact {

    @Id
//    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID artefactId;
    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date created;
    private String userId;
    private String category;
    private String description;


    //  The default constructor exists only for the sake of JPA. You do not use it directly, so it is designated as protected
    protected Artefact() {

    }

    public Artefact(UUID artefactId, Date created, String userId, String category, String description) {
        this.artefactId = artefactId;
        this.created = created;
        this.userId = userId;
        this.category = category;
        this.description = description;
    }


    public UUID getArtefactId() {
        return artefactId;
    }

    public void setArtefactId(UUID id) {
        this.artefactId = id;
    }

    @Column(name = "created", nullable = false)
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Column(name = "userId", nullable = false)
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Column(name = "category", nullable = false)
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Column(name = "description", nullable = false)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}