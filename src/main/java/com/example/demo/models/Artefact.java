package com.example.demo.models;
// This import helps create tables in database

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID artefactId;

    @CreatedDate
    @Temporal(TIMESTAMP)
    @Column(name = "created", nullable = false)
    private Date created;

    @Column(name = "userId", nullable = false)
    private String userId;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "description", nullable = false)
    private String description;

    //    @OneToMany(mappedBy = "artefact", fetch=FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "artefactId", referencedColumnName = "artefactId")
    private List<Commentary> commentaries = new ArrayList<>();


    //  The default constructor exists only for the sake of JPA. You do not use it directly, so it is designated as protected
    public Artefact() {

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

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List < Commentary > getCommentaries() {
        return commentaries;
    }

    public void setCommentaries(List < Commentary > commentaries) {
        this.commentaries = commentaries;
    }

}