package com.mparaske.studentmanagement.controller;

import com.mparaske.studentmanagement.model.Student;
import com.mparaske.studentmanagement.model.StudentUpdateRequest;
import com.mparaske.studentmanagement.service.StudentServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class StudentController {

    private final StudentServiceImpl studentService;

    @Autowired
    public StudentController(StudentServiceImpl studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/students")
    public ResponseEntity<List<Student>> getAllStudents() {
        try {
            List<Student> students = studentService.getAllStudents();
            if (students.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(students, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/students/{email}")
    public ResponseEntity<Optional<Student>> getStudentByEmail(@PathVariable String email) {
        return new ResponseEntity<>(studentService.getStudentByEmail(email), HttpStatus.OK);
    }

    @GetMapping("/students/checkEmail/{email}")
    public ResponseEntity<Boolean> checkEmailExists(@PathVariable String email) {
        boolean exists = studentService.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }

    @PostMapping("/students")
    public ResponseEntity<String> createStudent(@Valid @RequestBody Student student, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(" | ")), HttpStatus.BAD_REQUEST);
        }
        try {
            studentService.createStudent(student);
            return new ResponseEntity<>("Student has been created successfully", HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while creating the student: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/students/{email}")
    public ResponseEntity<String> updateStudent(@Valid @PathVariable("email") String email, @Valid @RequestBody StudentUpdateRequest studentUpdateRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" | ")), HttpStatus.BAD_REQUEST);
        }
        try {
            boolean isUpdated = studentService.updateStudent(email, studentUpdateRequest);
            if (isUpdated) {
                return new ResponseEntity<>("Student has been updated successfully for student with email: " + email, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Student not found with email: " + email, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while updating the student: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/students/{email}")
    public ResponseEntity<String> deleteStudentByEmail(@PathVariable("email") String email, @RequestParam("reassignThesis") boolean reassignThesis) {
        try {
            studentService.deleteStudentByEmail(email, reassignThesis);
            return new ResponseEntity<>("Student with email: " + email + " has been deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while deleting the student: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/students")
    public ResponseEntity<String> deleteAllStudents() {
        try {
            studentService.deleteAllStudents();
            return new ResponseEntity<>("All students have been deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while deleting the students: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
