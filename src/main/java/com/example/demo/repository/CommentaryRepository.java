package com.example.demo.repository;

import com.example.demo.models.Commentary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CommentaryRepository extends JpaRepository<Commentary, UUID> {

    Page<Commentary> findByArtefactId(UUID artefactId, Pageable pageable);
    boolean existsByCommentaryId(UUID commentaryId);
}

