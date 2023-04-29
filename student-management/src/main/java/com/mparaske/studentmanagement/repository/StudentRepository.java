package com.mparaske.studentmanagement.repository;

import com.mparaske.studentmanagement.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends MongoRepository<Student, String> {

    Optional<Student> findByEmail(String email);

    void deleteByEmail(String email);

    boolean existsByEmail(String email);
}
