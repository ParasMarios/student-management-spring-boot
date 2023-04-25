package com.mparaske.studentmanagement.service;

import com.mongodb.client.result.UpdateResult;
import com.mparaske.studentmanagement.model.Thesis;
import com.mparaske.studentmanagement.model.ThesisUpdateRequest;
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
    public boolean updateThesisByTitle(String title, ThesisUpdateRequest thesisUpdateRequest) {
        Query query = new Query();
        query.addCriteria(Criteria.where("title").is(title));
        Update update = new Update();
        if (thesisUpdateRequest.getTitle() != null) {
            update.set("title", thesisUpdateRequest.getTitle());
        }

        if (thesisUpdateRequest.getDescription() != null) {
            update.set("description", thesisUpdateRequest.getDescription());
        }

        if (thesisUpdateRequest.getMaxNumberOfStudents() != null) {
            update.set("maxNumberOfStudents", thesisUpdateRequest.getMaxNumberOfStudents());
        }

        if (thesisUpdateRequest.getNecessaryKnowledge() != null) {
            update.set("necessaryKnowledge", thesisUpdateRequest.getNecessaryKnowledge());
        }

        if (thesisUpdateRequest.getDeliverables() != null) {
            update.set("deliverables", thesisUpdateRequest.getDeliverables());
        }

        if (thesisUpdateRequest.getBibliographicReferences() != null) {
            update.set("bibliographicReferences", thesisUpdateRequest.getBibliographicReferences());
        }

        if (thesisUpdateRequest.getStatus() != null) {
            update.set("status", thesisUpdateRequest.getStatus());
        }

        if (update.getUpdateObject().keySet().size() > 0) {
            UpdateResult result = mongoTemplate.updateFirst(query, update, Thesis.class);
            return result.getModifiedCount() > 0;
        } else {
            return false;
        }
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
