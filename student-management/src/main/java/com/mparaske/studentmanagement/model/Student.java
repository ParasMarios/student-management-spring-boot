package com.mparaske.studentmanagement.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Data
@Document(collection = "students")
public class Student {

    @Id
    private String Id;
    private String firstName;
    private String lastName;
    private String email;
    private String thesisTitle;
    private String comments;
}
