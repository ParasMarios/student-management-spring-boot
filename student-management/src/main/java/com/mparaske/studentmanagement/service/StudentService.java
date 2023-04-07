package com.mparaske.studentmanagement.service;

import com.mparaske.studentmanagement.model.Student;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StudentService {

    List<Student> getAllStudents();

    Student getStudentByEmail(String email);

    Student createStudent(Student student);

    Student updateStudent(Student student);

    void deleteStudentByEmail(String email);
}
