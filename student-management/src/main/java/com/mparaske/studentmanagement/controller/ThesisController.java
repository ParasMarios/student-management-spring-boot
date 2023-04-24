package com.mparaske.studentmanagement.controller;

import com.mparaske.studentmanagement.model.Thesis;
import com.mparaske.studentmanagement.service.ThesisServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/theses/{title}")
    public ResponseEntity<Thesis> getThesisByTitle(@PathVariable String title) {
        return new ResponseEntity<>(thesisService.getThesisByTitle(title), HttpStatus.OK);
    }

    @PostMapping("/theses")
    public ResponseEntity<Thesis> createThesis(@RequestBody Thesis thesis) {
        return new ResponseEntity<>(thesisService.createThesis(thesis), HttpStatus.CREATED);
    }

    @PatchMapping("/theses/{title}")
    public ResponseEntity<String> updateThesisByTitle(@PathVariable("title") String title, @RequestBody Thesis updatedThesis) {
        try {
            boolean isUpdated = thesisService.updateThesisByTitle(title, updatedThesis);
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
