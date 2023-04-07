package com.mparaske.studentmanagement.service;

import com.mparaske.studentmanagement.model.Thesis;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ThesisService {

    List<Thesis> getAllTheses();

    Thesis getThesisByTitle(String title);

    Thesis createThesis(Thesis thesis);

    Thesis updateThesis(Thesis thesis);

    void deleteThesis(String id);
}
