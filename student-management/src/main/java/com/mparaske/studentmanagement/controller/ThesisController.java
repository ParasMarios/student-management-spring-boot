package com.mparaske.studentmanagement.controller;

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
@CrossOrigin(origins = "http://localhost:3000")
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

    @GetMapping("/theses/{title}")
    public ResponseEntity<Thesis> getThesisByTitle(@PathVariable String title) {
        return new ResponseEntity<>(thesisService.getThesisByTitle(title), HttpStatus.OK);
    }

    @GetMapping("/theses/status/contains/{status}")
    public ResponseEntity<List<Thesis>> getThesesByStatusContains(@PathVariable String status) {
        return new ResponseEntity<>(thesisService.getThesesByStatusContains(status), HttpStatus.OK);
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

    @PatchMapping("/theses/{title}")
    public ResponseEntity<String> updateThesisByTitle(@Valid @PathVariable("title") String title, @Valid @RequestBody ThesisUpdateRequest thesisUpdateRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" | ")), HttpStatus.BAD_REQUEST);
        }
        try {
            boolean isUpdated = thesisService.updateThesisByTitle(title, thesisUpdateRequest);
            if (isUpdated) {
                return new ResponseEntity<>("Thesis has been updated successfully for thesis with title: " + title, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Thesis not found with title: " + title, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while updating the thesis: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/theses/{title}")
    public ResponseEntity<String> deleteThesisByTitle(@PathVariable String title) {
        try {
            thesisService.deleteThesisByTitle(title);
            return new ResponseEntity<>("Thesis has been deleted successfully for thesis with title: " + title, HttpStatus.OK);
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
