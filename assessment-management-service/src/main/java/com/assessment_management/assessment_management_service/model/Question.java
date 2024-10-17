package com.assessment_management.assessment_management_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Question {

    private String id;
    private String questionText;
    private List<String> options;   // For multiple-choice questions
    private String correctAnswer;   // Only for multiple-choice


}
