package com.mparaske.studentmanagement.service;

import com.mparaske.studentmanagement.model.Student;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface StudentService {

    List<Student> getAllStudents();

    Optional<Student> getStudentByEmail(String email);

    Student createStudent(Student student);

    boolean updateThesisTitle(String email, String thesisTitle);

    void deleteStudentByEmail(String email);
}
