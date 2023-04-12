package com.mparaske.studentmanagement.controller;

import com.mparaske.studentmanagement.model.Thesis;
import com.mparaske.studentmanagement.service.ThesisServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/theses")
public class ThesisController {


    private final ThesisServiceImpl thesisService;

    @Autowired
    public ThesisController(ThesisServiceImpl thesisService) {
        this.thesisService = thesisService;
    }

    @GetMapping
    public ResponseEntity<List<Thesis>> getAllThesis() {
        return ResponseEntity.ok(thesisService.getAllTheses());
    }

    @GetMapping("/{title}")
    public ResponseEntity<Thesis> getThesisByTitle(@PathVariable String title) {
        return ResponseEntity.ok(thesisService.getThesisByTitle(title));
    }

    @PostMapping
    public ResponseEntity<Thesis> createThesis(@RequestBody Thesis thesis) {
        return ResponseEntity.ok(thesisService.createThesis(thesis));
    }

    @PatchMapping("/{title}")
    public ResponseEntity<String> updateThesisByTitle(@PathVariable("title") String title, @RequestBody Thesis updatedThesis) {
        try {
            boolean isUpdated = thesisService.updateThesisByTitle(title, updatedThesis);
            if (isUpdated) {
                return ResponseEntity.ok("Thesis has been updated successfully for thesis with title: " + title);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An error occurred while updating the thesis: " + e.getMessage());
        }
    }


    @DeleteMapping("/{title}")
    public ResponseEntity<String> deleteThesisByTitle(@PathVariable String title) {
        try {
            thesisService.deleteThesisByTitle(title);
            return ResponseEntity.ok("Thesis with title: " + title + " has been deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An error occurred while deleting the thesis: " + e.getMessage());
        }
    }
}
