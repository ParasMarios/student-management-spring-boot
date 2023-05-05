package com.mparaske.studentmanagement.service;

import com.mparaske.studentmanagement.model.Thesis;
import com.mparaske.studentmanagement.model.ThesisUpdateRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ThesisService {

    List<Thesis> getAllTheses();

    Thesis getThesisByTitle(String title);

    Thesis createThesis(Thesis thesis);

    boolean updateThesisByTitle(String title, ThesisUpdateRequest thesisUpdateRequest);

    void deleteThesisByTitle(String title);

    void deleteAllTheses();

    List<Thesis> getThesesByStatusContains(String status);
}
