package com.mparaske.studentmanagement.service;

import com.mongodb.client.result.UpdateResult;
import com.mparaske.studentmanagement.model.Student;
import com.mparaske.studentmanagement.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class StudentServiceImpl implements StudentService {


    private final StudentRepository studentRepository;

    private final MongoTemplate mongoTemplate;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository, MongoTemplate mongoTemplate) {
        this.studentRepository = studentRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Optional<Student> getStudentByEmail(String email) {
        return studentRepository.findByEmail(email);
    }

    @Override
    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public boolean updateThesisTitle(String email, String thesisTitle) {
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email));

        Update update = new Update();
        update.set("thesisTitle", thesisTitle);

        UpdateResult result = mongoTemplate.updateFirst(query, update, Student.class);

        return result.getModifiedCount() > 0;
    }

    @Override
    public void deleteStudentByEmail(String email) {
        studentRepository.deleteByEmail(email);
    }
}
