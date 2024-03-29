package com.mparaske.studentmanagement.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "theses")
public class Thesis {

    @Id
    private String id;

    @NotBlank(message = "Title cannot be empty")
    @Size(max = 100, message = "Title must be at most 100 characters")
    private String title;

    @NotBlank(message = "Description cannot be empty")
    @Size(max = 500, message = "Description must be at most 500 characters")
    private String description;

    @Min(value = 1, message = "Max number of students must be between 1 or 2")
    @Max(value = 2, message = "Max number of students must be between 1 or 2")
    private Integer maxNumberOfStudents;

    @Size(max = 100, message = "Necessary knowledge must be at most 100 characters")
    private String necessaryKnowledge;

    @Size(max = 100, message = "Deliverables must be at most 100 characters")
    private String deliverables;

    @Size(max = 100, message = "Bibliographic references must be at most 100 characters")
    private String bibliographicReferences;

    @NotBlank(message = "Status cannot be empty")
    @Size(max = 50, message = "Status must be at most 50 characters")
    private String status;

    @Size(max = 2, message = "Max number of assigned students must be between 1 or 2")
    private List<String> assignedStudents = new ArrayList<>();

    private List<Milestone> milestones = new ArrayList<>();

    public void setStatus(String status) {
        this.status = capitalizeFirstLetter(status);
    }

    private String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
}
