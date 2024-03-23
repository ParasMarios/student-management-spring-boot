package com.mparaske.studentmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Milestone {
    private String name;
    private String description;
    private String date;
    private String completionPercentage;
}
