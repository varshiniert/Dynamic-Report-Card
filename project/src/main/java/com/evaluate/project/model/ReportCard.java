package com.evaluate.project.model;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportCard {
    private String studentId;
    private List<Term> termScores;   // Term-wise subject scores
    private double finalScore;       // Overall final score
}
