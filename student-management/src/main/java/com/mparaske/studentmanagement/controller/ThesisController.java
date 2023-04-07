package com.mparaske.studentmanagement.controller;

import com.mparaske.studentmanagement.model.Thesis;
import com.mparaske.studentmanagement.service.ThesisServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Thesis> getAllThesis() {
        return thesisService.getAllTheses();
    }

    @GetMapping("/{title}")
    public Thesis getThesisByEmail(@PathVariable String title) {
        return thesisService.getThesisByTitle(title);
    }

    @PostMapping
    public Thesis createThesis(@RequestBody Thesis thesis) {
        return thesisService.createThesis(thesis);
    }

    @PutMapping("/{title}")
    public Thesis updateThesis(@RequestBody Thesis updatedThesis) {
        return thesisService.updateThesis(updatedThesis);
    }

    @DeleteMapping("/{title}")
    public void deleteThesis(@PathVariable String title) {
        thesisService.deleteThesisByTitle(title);
    }
}
