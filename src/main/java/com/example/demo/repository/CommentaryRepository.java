package com.example.demo.repository;

import com.example.demo.models.Commentary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CommentaryRepository extends JpaRepository<Commentary, UUID> {

    List<Commentary> findByArtefactId(UUID artefactId);
}

