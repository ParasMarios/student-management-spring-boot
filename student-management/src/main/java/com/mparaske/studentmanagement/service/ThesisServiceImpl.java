package com.mparaske.studentmanagement.service;

import com.mparaske.studentmanagement.model.Thesis;
import com.mparaske.studentmanagement.repository.ThesisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ThesisServiceImpl implements ThesisService {

    @Autowired
    private ThesisRepository thesisRepository;

    @Override
    public List<Thesis> getAllTheses() {
        return thesisRepository.findAll();
    }

    @Override
    public Thesis getThesisByTitle(String title) {
        return thesisRepository.findByTitle(title).orElse(null);
    }

    @Override
    public Thesis createThesis(Thesis thesis) {
        return thesisRepository.save(thesis);
    }

    @Override
    public Thesis updateThesis(Thesis thesis) {
        return thesisRepository.save(thesis);
    }

    @Override
    public void deleteThesis(String id) {
        thesisRepository.deleteById(id);
    }
}
