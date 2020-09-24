package com.example.demo.controllers;

import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.models.Artefact;
import com.example.demo.models.Commentary;
import com.example.demo.repository.ArtefactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1")
public class ArtefactController {

    @Autowired
    private ArtefactRepository artefactRepository;


    @GetMapping("/artefacts")
    public Page<Artefact> getAllArtefacts(Pageable pageable) {
        return artefactRepository.findAll(pageable);
    }

    @GetMapping("/artefacts/{id}/commentaries")
    public ResponseEntity<Page<Commentary>> getCommentariesByArtefactId(@PathVariable(value = "id") UUID artefactId, Pageable pageable)
            throws ResourceNotFoundException {
        Artefact artefact = artefactRepository.findById(artefactId)
                .orElseThrow(() -> new ResourceNotFoundException("Commentaries not found for this id :: " + artefactId));
        return ResponseEntity.ok().body(artefact.getCommentaries(pageable));
    }

    @GetMapping("/artefacts/{id}")
    public ResponseEntity<Artefact> getArtefactById(@PathVariable(value = "id") UUID artefactId)
            throws ResourceNotFoundException {
        Artefact artefact = artefactRepository.findById(artefactId)
                .orElseThrow(() -> new ResourceNotFoundException("Artefact not found for this id :: " + artefactId));
        return ResponseEntity.ok().body(artefact);
    }

    @PostMapping("/artefacts")
    public Artefact createArtefact(@Valid @RequestBody Artefact artefact) {
        return artefactRepository.save(artefact);
    }

    @PutMapping("/artefacts/{id}")
    public ResponseEntity<Artefact> updateArtefact(
            @PathVariable(value = "id") UUID artefactId,
            @Valid @RequestBody Artefact artefactDetails) throws ResourceNotFoundException {
        Artefact artefact = artefactRepository.findById(artefactId)
                .orElseThrow(() -> new ResourceNotFoundException("Artefact not found for this id :: " + artefactId));

        artefact.setCreated(artefactDetails.getCreated());
        artefact.setUserId(artefactDetails.getUserId());
        artefact.setCategory(artefactDetails.getCategory());
        artefact.setDescription(artefactDetails.getDescription());
//        artefact.setLastModifiedDate(new Date());
        final Artefact updatedArtefact = artefactRepository.save(artefact);
        return ResponseEntity.ok(updatedArtefact);
    }

    @DeleteMapping("/artefacts/{id}")
    public Map<String, Boolean> deleteArtefact(
            @PathVariable(value = "id") UUID artefactId) throws ResourceNotFoundException {
        Artefact artefact = artefactRepository.findById(artefactId)
                .orElseThrow(() -> new ResourceNotFoundException("Artefact not found for this id :: " + artefactId));

        artefactRepository.delete(artefact);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}