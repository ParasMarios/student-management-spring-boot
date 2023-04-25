package com.mparaske.studentmanagement.model;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class StudentUpdateRequest {

    @Size(min = 5, max = 200, message = "Thesis title must be between 5 and 200 characters")
    private String thesisTitle;

    @Size(max = 500, message = "Comments should not exceed 500 characters")
    private String comments;
}
