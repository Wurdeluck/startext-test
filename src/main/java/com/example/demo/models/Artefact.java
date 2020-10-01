package com.example.demo.models;
// This import helps create tables in database

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

// The Artefact class is annotated with @Entity, indicating that it is a JPA entity.
// @Table annotation exists, this entity is mapped to a table named artefacts.
@Entity
@Table(name = "artefacts")

public class Artefact {
    @Id
    private UUID artefactId;

    //    @CreatedDate
//    @Temporal(TIMESTAMP)
    @Column(name = "created")
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
        if (artefactId == null) {
            artefactId = UUID.randomUUID();
        }
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

    public Page<Commentary> getCommentaries(Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = (Math.min((start + pageable.getPageSize()), commentaries.size()));
        return new PageImpl<>(commentaries.subList(start, end), pageable, commentaries.size());
    }

    public void setCommentaries(List<Commentary> commentaries) {
        this.commentaries = commentaries;
    }

}