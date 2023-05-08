package com.mparaske.studentmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Milestone {

    private String name;
    private Date date;
    private int completionPercentage;
}
