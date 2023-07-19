package com.mparaske.studentmanagement.service;

import com.mparaske.studentmanagement.model.Thesis;
import com.mparaske.studentmanagement.model.ThesisUpdateRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ThesisService {

    List<Thesis> getAllTheses();

    Thesis getThesisById(String id);

    void createThesis(Thesis thesis);

    boolean updateThesisById(String id, ThesisUpdateRequest thesisUpdateRequest);

    void deleteThesisById(String id);

    void deleteAllTheses();

    List<Thesis> getThesesByTitleOrDescriptionContains(String keyword, String status);
}
