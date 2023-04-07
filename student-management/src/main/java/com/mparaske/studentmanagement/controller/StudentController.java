package com.mparaske.studentmanagement.controller;

import com.mparaske.studentmanagement.model.Student;
import com.mparaske.studentmanagement.service.StudentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {

    @Autowired
    private StudentServiceImpl studentService;

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        return new ResponseEntity<List<Student>>(studentService.getAllStudents(), HttpStatus.OK);
    }

    @GetMapping("/{email}")
    public Student getStudentById(@PathVariable String email) {
        return studentService.getStudentByEmail(email);
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @PutMapping("/{email}")
    public Student updateStudent(@RequestBody Student updatedStudent) {
        return studentService.updateStudent(updatedStudent);
    }

    @DeleteMapping("/{email}")
    public void deleteStudent(@PathVariable String id) {
        studentService.deleteStudent(id);
    }
}
