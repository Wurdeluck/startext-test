package com.example.demo.repository;

import com.example.demo.models.Artefact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ArtefactRepository extends JpaRepository<Artefact, UUID>, JpaSpecificationExecutor<Artefact> {
    boolean existsByArtefactId(UUID artefactId);
}