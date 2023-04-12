package com.mparaske.studentmanagement.service;

import com.mparaske.studentmanagement.model.Thesis;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ThesisService {

    List<Thesis> getAllTheses();

    Thesis getThesisByTitle(String title);

    Thesis createThesis(Thesis thesis);

    boolean updateThesisByTitle(String title, Thesis thesis);

    void deleteThesisByTitle(String title);
}
