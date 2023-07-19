package com.mparaske.studentmanagement.controller;

import com.mparaske.studentmanagement.exception.ThesisNotFoundException;
import com.mparaske.studentmanagement.model.Thesis;
import com.mparaske.studentmanagement.model.ThesisUpdateRequest;
import com.mparaske.studentmanagement.service.ThesisServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class ThesisController {


    private final ThesisServiceImpl thesisService;

    @Autowired
    public ThesisController(ThesisServiceImpl thesisService) {
        this.thesisService = thesisService;
    }

    @GetMapping("/theses")
    public ResponseEntity<List<Thesis>> getAllThesis() {
        try {
            List<Thesis> theses = thesisService.getAllTheses();
            if (theses.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(theses, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/theses/{id}")
    public ResponseEntity<?> getThesisById(@PathVariable String id) {
        Thesis thesis = thesisService.getThesisById(id);
        if (thesis != null) {
            return new ResponseEntity<>(thesis, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Thesis not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/theses/search/{keyword}")
    public ResponseEntity<List<Thesis>> getThesesByTitleOrDescriptionContains(@PathVariable String keyword, @RequestParam(required = false) String status) {
        return new ResponseEntity<>(thesisService.getThesesByTitleOrDescriptionContains(keyword, status), HttpStatus.OK);
    }

    @PostMapping("/theses")
    public ResponseEntity<String> createThesis(@Valid @RequestBody Thesis thesis, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" | ")), HttpStatus.BAD_REQUEST);
        }
        try {
            thesisService.createThesis(thesis);
            return new ResponseEntity<>("Thesis has been created successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while creating the thesis: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/theses/{id}")
    public ResponseEntity<String> updateThesisById(@Valid @PathVariable("id") String id, @Valid @RequestBody ThesisUpdateRequest thesisUpdateRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" | ")), HttpStatus.BAD_REQUEST);
        }
        try {
            boolean isUpdated = thesisService.updateThesisById(id, thesisUpdateRequest);
            if (isUpdated) {
                return new ResponseEntity<>("Thesis has been updated successfully with id: " + id, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Thesis not found with id: " + id, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while updating the thesis: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/theses/{id}")
    public ResponseEntity<String> deleteThesisById(@PathVariable String id) {
        try {
            thesisService.deleteThesisById(id);
            return new ResponseEntity<>("Thesis deleted successfully", HttpStatus.OK);
        } catch (ThesisNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while deleting the thesis: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/theses")
    public ResponseEntity<String> deleteAllTheses() {
        try {
            thesisService.deleteAllTheses();
            return new ResponseEntity<>("All theses have been deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while deleting the theses: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
