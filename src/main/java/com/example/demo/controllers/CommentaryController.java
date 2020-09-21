package com.example.demo.controllers;

import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.models.Commentary;
import com.example.demo.repository.CommentaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/")

public class CommentaryController {

    @Autowired
    private CommentaryRepository commentaryRepository;

    @GetMapping("/commentaries")
    public List<Commentary> getAllCommentaries() {
        return commentaryRepository.findAll();
    }

    @GetMapping("/artefacts/{artefactId}/commentaries_alternative")
    public List<Commentary> getAllCommentariesInArtefact(@PathVariable(value = "artefactId") UUID artefactId) {
        return commentaryRepository.findByArtefactId(artefactId);
    }

    @GetMapping("/commentaries/{id}")
    public ResponseEntity<Commentary> getCommentaryById(@PathVariable(value = "id") UUID commentaryId)
            throws ResourceNotFoundException {
        Commentary commentary = commentaryRepository.findById(commentaryId)
                .orElseThrow(() -> new ResourceNotFoundException("Commentary not found for this id :: " + commentaryId));
        return ResponseEntity.ok().body(commentary);
    }

    @PostMapping("/commentaries")
    public Commentary createCommentary(@Valid @RequestBody Commentary commentary) {
        return commentaryRepository.save(commentary);
    }

    @PutMapping("/commentaries/{id}")
    public ResponseEntity<Commentary> updateCommentary(
            @PathVariable(value = "id") UUID commentaryId,
            @Valid @RequestBody Commentary commentaryDetails) throws ResourceNotFoundException {
        Commentary commentary = commentaryRepository.findById(commentaryId)
                .orElseThrow(() -> new ResourceNotFoundException("Commentary not found for this id :: " + commentaryId));
        commentary.setUserId(commentaryDetails.getUserId());
        commentary.setContent(commentaryDetails.getContent());
        commentary.setArtefactId(commentaryDetails.getArtefactId());
        final Commentary updatedCommentary = commentaryRepository.save(commentary);
        return ResponseEntity.ok().body(updatedCommentary);
    }

    @DeleteMapping("/commentaries/{id}")
    public Map<String, Boolean> deleteCommentary(
            @PathVariable(value = "id") UUID commentaryId) throws ResourceNotFoundException {
        Commentary commentary = commentaryRepository.findById(commentaryId)
                .orElseThrow(() -> new ResourceNotFoundException("Commentary not found for this id :: " + commentaryId));

        commentaryRepository.delete(commentary);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}


