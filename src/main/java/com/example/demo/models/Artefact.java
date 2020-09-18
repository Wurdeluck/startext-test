package com.example.demo.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "artefacts")
public class Artefact {

    private UUID artefactId;
    private Date created;
    private String userId;
    private String category;
    private String description;

    public Artefact() {

    }

    public Artefact(UUID artefactId, Date created, String userId, String category, String description) {
        this.artefactId = artefactId;
        this.created = created;
        this.userId = userId;
        this.category = category;
        this.description = description;
    }

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
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