package com.mparaske.studentmanagement.service;

import com.mongodb.client.result.UpdateResult;
import com.mparaske.studentmanagement.model.Student;
import com.mparaske.studentmanagement.model.StudentUpdateRequest;
import com.mparaske.studentmanagement.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

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
        if (isValidEmail(student.getEmail())) {
            return studentRepository.save(student);
        } else {
            throw new IllegalArgumentException("Invalid email");
        }
    }

    @Override
    public boolean updateStudent(String email, StudentUpdateRequest studentUpdateRequest) {
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email));

        Update update = new Update();
        if (studentUpdateRequest.getThesisTitle() != null) {
            update.set("thesisTitle", studentUpdateRequest.getThesisTitle());
        }
        if (studentUpdateRequest.getComments() != null) {
            update.set("comments", studentUpdateRequest.getComments());
        }

        if (update.getUpdateObject().keySet().size() > 0) {
            UpdateResult result = mongoTemplate.updateFirst(query, update, Student.class);
            return result.getModifiedCount() > 0;
        }
        return false;
    }

    @Override
    public void deleteStudentByEmail(String email) {
        studentRepository.deleteByEmail(email);
    }

    @Override
    public void deleteAllStudents() {
        studentRepository.deleteAll();
    }

    @Override
    public boolean isValidEmail(String email) {
        try {
            InternetAddress emailAddress = new InternetAddress(email);
            emailAddress.validate();
            return email.endsWith("@uop.gr");
        } catch (AddressException ex) {
            return false;
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        return studentRepository.existsByEmail(email);
    }
}
