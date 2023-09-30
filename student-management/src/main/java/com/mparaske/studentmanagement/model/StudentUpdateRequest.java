package com.mparaske.studentmanagement.model;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class StudentUpdateRequest {

    @Size(min = 5, max = 200, message = "Thesis title must be between 5 and 200 characters")
    private String thesisTitle;

    private Date lastModifiedDate;

    private List<Comment> comments = new ArrayList<>();
}
