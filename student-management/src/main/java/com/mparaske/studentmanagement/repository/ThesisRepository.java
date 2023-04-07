package com.mparaske.studentmanagement.repository;

import com.mparaske.studentmanagement.model.Thesis;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ThesisRepository extends MongoRepository<Thesis, String> {

    Optional<Thesis> findByTitle(String title);
}
