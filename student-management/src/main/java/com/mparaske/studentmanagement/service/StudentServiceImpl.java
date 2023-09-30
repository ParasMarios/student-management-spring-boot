package com.mparaske.studentmanagement.service;

import com.mongodb.client.result.UpdateResult;
import com.mparaske.studentmanagement.model.Comment;
import com.mparaske.studentmanagement.model.Student;
import com.mparaske.studentmanagement.model.StudentUpdateRequest;
import com.mparaske.studentmanagement.model.Thesis;
import com.mparaske.studentmanagement.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class StudentServiceImpl implements StudentService {


    private final StudentRepository studentRepository;

    private final MongoTemplate mongoTemplate;

    @Value("${allowed.email.patterns}")
    private String[] allowedEmailPatterns;

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
    public void createStudent(Student student) {
        if (isValidEmail(student.getEmail())) {
            Thesis existingThesis = mongoTemplate.findOne(Query.query(Criteria.where("title").is(student.getThesisTitle())), Thesis.class, "theses");
            if (existingThesis != null) {
                if (existingThesis.getStatus().equals("Available") || (existingThesis.getStatus().equals("Assigned") && existingThesis.getAssignedStudents().size() < existingThesis.getMaxNumberOfStudents())) {
                    existingThesis.getAssignedStudents().add(student.getEmail());
                    existingThesis.setStatus("Assigned");
                    mongoTemplate.save(existingThesis);
                    List<Comment> comments = student.getComments();
                    student.setComments(comments);
                    studentRepository.save(student);
                } else {
                    throw new IllegalArgumentException("The thesis is not available for assignment.");
                }
            } else {
                throw new IllegalArgumentException("The thesis with the given title does not exist.");
            }
        } else {
            throw new IllegalArgumentException("Invalid email");
        }
    }

    @Override
    public boolean updateStudent(String email, StudentUpdateRequest studentUpdateRequest) {
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email));
        Student existingStudent = mongoTemplate.findOne(query, Student.class);

        // Add a null check for existingStudent
        if (existingStudent == null) {
            throw new IllegalArgumentException("The student with the given email does not exist.");
        }

        Update update = new Update();
        Date currentDate = new Date(); // Get the current date

        if (studentUpdateRequest.getThesisTitle() != null && !studentUpdateRequest.getThesisTitle().equals(existingStudent.getThesisTitle())) {
            // Unassign from old thesis
            if (existingStudent.getThesisTitle() != null) {
                Thesis oldThesis = mongoTemplate.findOne(Query.query(Criteria.where("title").is(existingStudent.getThesisTitle())), Thesis.class, "theses");
                // Add a null check for oldThesis
                if (oldThesis != null) {
                    oldThesis.getAssignedStudents().remove(email);
                    if (oldThesis.getAssignedStudents().isEmpty()) {
                        oldThesis.setStatus("Available");
                    }
                    mongoTemplate.save(oldThesis);
                }
            }

            // Assign to new thesis
            Thesis newThesis = mongoTemplate.findOne(Query.query(Criteria.where("title").is(studentUpdateRequest.getThesisTitle())), Thesis.class, "theses");
            // Add a null check for newThesis
            if (newThesis != null) {
                if (newThesis.getAssignedStudents().size() < newThesis.getMaxNumberOfStudents()) {
                    newThesis.getAssignedStudents().add(email);
                    newThesis.setStatus("Assigned");
                    mongoTemplate.save(newThesis);
                    update.set("thesisTitle", studentUpdateRequest.getThesisTitle());
                } else {
                    throw new IllegalArgumentException("The thesis is already assigned to the maximum number of students.");
                }
            } else {
                throw new IllegalArgumentException("The thesis with the given title does not exist.");
            }
        }

        if (studentUpdateRequest.getComments() != null) {
            List<Comment> updatedComments = studentUpdateRequest.getComments();
            update.set("comments", updatedComments);
        }

        // Update the last modified date
        update.set("lastModifiedDate", currentDate);

        if (!update.getUpdateObject().keySet().isEmpty()) {
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
                            existingThesis.setStatus("Available");
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
        for (String pattern : allowedEmailPatterns) {
            if (email.endsWith(pattern)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean existsByEmail(String email) {
        return studentRepository.existsByEmail(email);
    }
}
