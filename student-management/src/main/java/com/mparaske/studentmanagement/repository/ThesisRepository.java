package com.mparaske.studentmanagement.repository;

import com.mongodb.lang.Nullable;
import com.mparaske.studentmanagement.model.Thesis;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ThesisRepository extends MongoRepository<Thesis, String> {

    @Query("{ '_id' : ?0 }")
    Thesis findThesisById(String id);

    void deleteById(@Nullable String id);
}
