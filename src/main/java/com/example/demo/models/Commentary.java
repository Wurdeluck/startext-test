package com.example.demo.models;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "commentaries")

public class Commentary {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID commentaryId;

    @Column(name = "userId", nullable = false)
    private String userId;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "artefactId", nullable = false)
    private UUID artefactId;

//    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Artefact.class)
//    @JoinColumn(name = "artefactId", referencedColumnName = "artefactId", nullable = false)
//    private Artefact artefact;


    public Commentary() {

    }

    public Commentary(UUID commentaryId, UUID artefactId, String userId, String content) {
        this.commentaryId = commentaryId;
        this.userId = userId;
        this.content = content;
        this.artefactId = artefactId;
    }

    public UUID getCommentaryId() {
        return commentaryId;
    }

    public void setCommentaryId(UUID commentaryId) {
        this.commentaryId = commentaryId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UUID getArtefactId() {
        return artefactId;
    }

    public void setArtefactId(UUID artefactId) {
        this.artefactId = artefactId;
    }

    //    @Override
//    public boolean equals(Object o) {
//        if (this == o)
//            return true;
//
//        if (!(o instanceof Commentary))
//            return false;
//
//        return
//                commentaryId != null &&
//                        commentaryId.equals(((Commentary) o).getCommentaryId());
//    }
//
//    @Override
//    public int hashCode() {
//        return 31;
//    }
//
//    public void setArtefact(Artefact artefact) {
//    }
}
