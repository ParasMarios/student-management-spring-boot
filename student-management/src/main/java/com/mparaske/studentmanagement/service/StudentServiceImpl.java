package com.mparaske.studentmanagement.service;

import com.mongodb.client.result.UpdateResult;
import com.mparaske.studentmanagement.model.Student;
import com.mparaske.studentmanagement.model.StudentUpdateRequest;
import com.mparaske.studentmanagement.model.Thesis;
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
            Thesis existingThesis = mongoTemplate.findOne(Query.query(Criteria.where("title").is(student.getThesisTitle())), Thesis.class, "theses");
            if (existingThesis != null) {
                if (existingThesis.getAssignedStudents().size() < existingThesis.getMaxNumberOfStudents()) {
                    existingThesis.getAssignedStudents().add(student.getEmail());
                    existingThesis.setStatus("Assigned");
                    mongoTemplate.save(existingThesis);
                } else {
                    throw new IllegalArgumentException("The thesis is already assigned to the maximum number of students.");
                }
            } else {
                throw new IllegalArgumentException("The thesis with the given title does not exist.");
            }
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
    public void deleteStudentByEmail(String email, boolean reassignThesis) {
        Optional<Student> studentOptional = studentRepository.findByEmail(email);
        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            Thesis existingThesis = mongoTemplate.findOne(Query.query(Criteria.where("title").is(student.getThesisTitle())), Thesis.class, "theses");
            if (existingThesis != null) {
                boolean removed = existingThesis.getAssignedStudents().removeIf(email::equals);
                if (removed) {
                    if (existingThesis.getAssignedStudents().isEmpty()) {
                        if (reassignThesis) {
                            existingThesis.setStatus("available");
                            existingThesis.getMilestones().clear();
                        } else {
                            mongoTemplate.remove(existingThesis);
                            studentRepository.deleteByEmail(email);
                            return;
                        }
                    }
                    mongoTemplate.save(existingThesis);
                }
            }
            studentRepository.deleteByEmail(email);
        } else {
            throw new IllegalArgumentException("Student not found with the provided email");
        }
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
