package com.mparaske.studentmanagement.service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.result.UpdateResult;
import com.mparaske.studentmanagement.exception.ThesisNotFoundException;
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
import java.util.Optional;

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
    public Thesis getThesisById(String id) {
        return thesisRepository.findThesisById(id);
    }

    @Override
    public void createThesis(Thesis thesis) {
        thesisRepository.save(thesis);
    }

    @Override
    public boolean updateThesisById(String id, ThesisUpdateRequest thesisUpdateRequest) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
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
        if (thesisUpdateRequest.getMilestones() != null) {
            List<DBObject> dbMilestones = thesisUpdateRequest.getMilestones().stream()
                    .map(milestone -> {
                        DBObject dbObject = new BasicDBObject();
                        dbObject.put("name", milestone.getName());
                        dbObject.put("description", milestone.getDescription());
                        dbObject.put("date", milestone.getDate());
                        dbObject.put("completionPercentage", milestone.getCompletionPercentage());
                        return dbObject;
                    })
                    .toList();

            for (DBObject dbMilestone : dbMilestones) {
                update.push("milestones", dbMilestone);
            }
        }


        if (update.getUpdateObject().keySet().size() > 0) {
            UpdateResult result = mongoTemplate.updateFirst(query, update, Thesis.class);
            return result.getModifiedCount() > 0;
        } else {
            return false;
        }
    }

    @Override
    public void deleteThesisById(String id) {
        Optional<Thesis> thesis = thesisRepository.findById(id);
        if (thesis.isPresent()) {
            thesisRepository.deleteById(id);
        } else {
            throw new ThesisNotFoundException("Thesis not found");
        }
    }

    @Override
    public void deleteAllTheses() {
        thesisRepository.deleteAll();
    }

    @Override
    public List<Thesis> getThesesByTitleOrDescriptionContains(String keyword, String status) {
        Query query = new Query();
        Criteria criteria = new Criteria().orOperator(
                Criteria.where("title").regex(keyword, "i"),
                Criteria.where("description").regex(keyword, "i")
        );
        if (status != null && !status.isEmpty()) {
            criteria.and("status").is(status);
        }
        query.addCriteria(criteria);
        return mongoTemplate.find(query, Thesis.class);
    }
}
