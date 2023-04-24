package com.mparaske.studentmanagement.service;

import com.mongodb.client.result.UpdateResult;
import com.mparaske.studentmanagement.model.Thesis;
import com.mparaske.studentmanagement.repository.ThesisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ThesisServiceImpl implements ThesisService {


    private final ThesisRepository thesisRepository;

    private final MongoTemplate mongoTemplate;

    @Autowired
    public ThesisServiceImpl(ThesisRepository thesisRepository, MongoTemplate mongoTemplate) {
        this.thesisRepository = thesisRepository;
        this.mongoTemplate = mongoTemplate;
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
    public boolean updateThesisByTitle(String title, Thesis thesis) {
        Query query = new Query();
        query.addCriteria(Criteria.where("title").is(title));
        Update update = new Update();
        if (thesis.getTitle() != null) {
            update.set("title", thesis.getTitle());
        }

        if (thesis.getDescription() != null) {
            update.set("description", thesis.getDescription());
        }

        if (thesis.getMaxNumberOfStudents() != null) {
            update.set("maxNumberOfStudents", thesis.getMaxNumberOfStudents());
        }

        if (thesis.getNecessaryKnowledge() != null) {
            update.set("necessaryKnowledge", thesis.getNecessaryKnowledge());
        }

        if (thesis.getDeliverables() != null) {
            update.set("deliverables", thesis.getDeliverables());
        }

        if (thesis.getBibliographicReferences() != null) {
            update.set("bibliographicReferences", thesis.getBibliographicReferences());
        }

        if (thesis.getStatus() != null) {
            update.set("status", thesis.getStatus());
        }

        UpdateResult result = mongoTemplate.updateFirst(query, update, Thesis.class);

        return result.getModifiedCount() > 0;
    }

    @Override
    public void deleteThesisByTitle(String title) {
        thesisRepository.deleteByTitle(title);
    }

    @Override
    public void deleteAllTheses() {
        thesisRepository.deleteAll();
    }
}
