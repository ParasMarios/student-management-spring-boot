package com.mparaske.studentmanagement.service;

import com.mparaske.studentmanagement.model.Student;
import com.mparaske.studentmanagement.model.StudentUpdateRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface StudentService {

    List<Student> getAllStudents();

    Optional<Student> getStudentByEmail(String email);

    void createStudent(Student student);

    boolean updateStudent(String email, StudentUpdateRequest studentUpdateRequest);

    void deleteStudentByEmail(String email, boolean reassignThesis);

    void deleteAllStudents();

    boolean isValidEmail(String email);

    boolean existsByEmail(String email);
}
