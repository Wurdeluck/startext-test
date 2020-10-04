package com.example.demo.controllers;

import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.models.Artefact;
import com.example.demo.models.Commentary;
import com.example.demo.repository.ArtefactRepository;
import com.example.demo.specifications.ArtefactSpecificationBuilder;
import com.example.demo.util.SearchOperation;
import com.sipios.springsearch.anotation.SearchSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@RestController
@RequestMapping("/api/v1")
public class ArtefactController {

    @Autowired
    private ArtefactRepository artefactRepository;

//
//    @GetMapping("/artefacts")
//    public Page<Artefact> getAllArtefacts(Pageable pageable) {
//        return artefactRepository.findAll(pageable);
//    }

    @GetMapping("/artefacts")
    public Page<Artefact> search(@RequestParam(value = "search", required = false) String search, Pageable pageable) {
        ArtefactSpecificationBuilder builder = new ArtefactSpecificationBuilder();
        String operationSetExper = String.join("|", SearchOperation.SIMPLE_OPERATION_SET);
        Pattern pattern = Pattern.compile("(\\w+?)(" + operationSetExper + ")(\\p{Punct}?)(\\w+?)(\\p{Punct}?),");
        Matcher matcher = pattern.matcher(search + ",");
        while (matcher.find()) {
            builder.with(matcher.group(1), matcher.group(2), matcher.group(4), matcher.group(3), matcher.group(5));
        }
        Specification<Artefact> spec = builder.build();
        return artefactRepository.findAll(spec, pageable);
    }

    @GetMapping("/artefacts_alternative")
    public Page<Artefact> search(@SearchSpec Specification<Artefact> specs, Pageable pageable) {
        return artefactRepository.findAll(Specification.where(specs), pageable);
    }

    @GetMapping("/artefacts/{id}/commentaries")
    public ResponseEntity<Page<Commentary>> getCommentariesByArtefactId(@PathVariable(value = "id") UUID artefactId, Pageable pageable)
            throws ResourceNotFoundException {
        Artefact artefact = artefactRepository.findById(artefactId)
                .orElseThrow(() -> new ResourceNotFoundException("Commentaries not found. Artefact does not exist for this id :: " + artefactId));
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
    public Artefact createArtefact(@Valid @RequestBody Artefact artefact) throws EntityExistsException {
        UUID artefactId = artefact.getArtefactId();
        artefact.setCreated(new Date());
        if (artefactRepository.existsByArtefactId(artefactId)) {
            throw new EntityExistsException("Artefact with this id already exists in database :: "+ artefactId);
        }

        return artefactRepository.save(artefact);
    }

    @PutMapping("/artefacts/{id}")
    public ResponseEntity<Artefact> updateArtefact(
            @PathVariable(value = "id") UUID artefactId,
            @Valid @RequestBody Artefact artefactDetails) throws ResourceNotFoundException {
        Artefact artefact = artefactRepository.findById(artefactId)
                .orElseThrow(() -> new ResourceNotFoundException("Artefact not found for this id :: " + artefactId));
//        artefact.setArtefactId(artefactDetails.getArtefactId());
//        artefact.setCreated(artefactDetails.getCreated());
//        artefact.setUserId(artefactDetails.getUserId());
        artefact.setCategory(artefactDetails.getCategory());
        artefact.setDescription(artefactDetails.getDescription());
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