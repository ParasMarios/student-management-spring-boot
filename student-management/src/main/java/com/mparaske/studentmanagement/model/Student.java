package com.mparaske.studentmanagement.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "students")
public class Student {

    @Id
    private String Id;

    @NotBlank(message = "First name cannot be blank")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    @Pattern(regexp = ".+@uop\\.gr$", message = "Email must end with @uop.gr")
    private String email;

    @NotBlank(message = "Thesis title cannot be blank")
    @Size(min = 5, max = 200, message = "Thesis title must be between 5 and 200 characters")
    private String thesisTitle;

    @Size(max = 500, message = "Comments should not exceed 500 characters")
    private String comments;
}
