package com.mparaske.studentmanagement.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ThesisUpdateRequest {

    @Size(max = 100, message = "Title must be at most 100 characters")
    private String title;

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

    @Size(max = 50, message = "Status must be at most 50 characters")
    private String status;

    private List<Milestone> milestones = new ArrayList<>();

    private Integer deletedMilestoneIndex;
}
