package com.mparaske.studentmanagement.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "theses")
public class Thesis {

    @Id
    private String id;
    private String title;
    private String description;
    private int maxNumberOfStudents;
    private String necessaryKnowledge;
    private String deliverables;
    private String bibliographicReferences;
    private String status;
}
