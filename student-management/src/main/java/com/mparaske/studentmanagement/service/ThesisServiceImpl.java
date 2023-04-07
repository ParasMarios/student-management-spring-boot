package com.mparaske.studentmanagement.service;

import com.mparaske.studentmanagement.model.Thesis;
import com.mparaske.studentmanagement.repository.ThesisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ThesisServiceImpl implements ThesisService {


    private final ThesisRepository thesisRepository;

    @Autowired
    public ThesisServiceImpl(ThesisRepository thesisRepository) {
        this.thesisRepository = thesisRepository;
    }

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
    public void deleteThesisByTitle(String title) {
        thesisRepository.deleteByTitle(title);
    }
}
