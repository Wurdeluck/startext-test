package com.example.demo.controllers;

import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.models.Commentary;
import com.example.demo.repository.CommentaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/")

public class CommentaryController {

    @Autowired
    private CommentaryRepository commentaryRepository;

//    @GetMapping("/commentaries")
//    public List<Commentary> getAllCommentaries() {
//        return commentaryRepository.findAll();
//    }

//    @GetMapping("/artefacts/{artefactId}/commentaries_alternative")
//    public List<Commentary> getAllCommentariesInArtefact(@PathVariable(value = "artefactId") UUID artefactId) {
//        return commentaryRepository.findByArtefactId(artefactId);
//    }

    @GetMapping("/commentaries")
    public Page<Commentary> getAllCommentariesInArtefact(@RequestParam(value = "artefactId", required = false) UUID artefactId, Pageable pageable)
            throws ResourceNotFoundException {
        if (artefactId != null) {
            Page<Commentary> commentaries =  commentaryRepository.findByArtefactId(artefactId, pageable);
            if (commentaries.isEmpty()) {
                throw new ResourceNotFoundException("Artefact was not found for this id :: " + artefactId);
            }
            return commentaries;
/*
            List<Commentary> commentaries =  commentaryRepository.findByArtefactId(artefactId);
            if (commentaries.isEmpty()) {
                throw new ResourceNotFoundException("Artefact was not found for this id :: " + artefactId);
            }
            int start = (int) pageable.getOffset();
            int end = (int) (Math.min((start + pageable.getPageSize()), commentaries.size()));
            Page<Commentary> page
                    = new PageImpl<Commentary>(commentaries.subList(start, end), pageable, commentaries.size());
            return page;
*/

        } else {
            return commentaryRepository.findAll(pageable);
        }
    }

    @GetMapping("/commentaries/{id}")
    public ResponseEntity<Commentary> getCommentaryById(@PathVariable(value = "id") UUID commentaryId)
            throws ResourceNotFoundException {
        Commentary commentary = commentaryRepository.findById(commentaryId)
                .orElseThrow(() -> new ResourceNotFoundException("Commentary not found for this id :: " + commentaryId));
        return ResponseEntity.ok().body(commentary);
    }

    @PostMapping("/commentaries")
    @ResponseStatus(HttpStatus.CREATED)
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


