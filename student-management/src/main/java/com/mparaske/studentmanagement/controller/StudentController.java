package com.mparaske.studentmanagement.controller;

import com.mparaske.studentmanagement.model.Student;
import com.mparaske.studentmanagement.service.StudentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {


    private final StudentServiceImpl studentService;

    @Autowired
    public StudentController(StudentServiceImpl studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        return new ResponseEntity<>(studentService.getAllStudents(), HttpStatus.OK);
    }

    @GetMapping("/{email}")
    public ResponseEntity<Optional<Student>> getStudentByEmail(@PathVariable String email) {
        return new ResponseEntity<>(studentService.getStudentByEmail(email), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        try {
            return new ResponseEntity<>(studentService.createStudent(student), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{email}")
    public ResponseEntity<String> updateStudent(@PathVariable("email") String email, @RequestBody Student student) {
        try {
            boolean isUpdated = studentService.updateStudent(email, student);
            if (isUpdated) {
                return new ResponseEntity<>("Student has been updated successfully for student with email: " + email, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Student not found with email: " + email, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while updating the student: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<String> deleteStudentByEmail(@PathVariable String email) {
        try {
            studentService.deleteStudentByEmail(email);
            return new ResponseEntity<>("Student with email: " + email + " has been deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while deleting the student: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
