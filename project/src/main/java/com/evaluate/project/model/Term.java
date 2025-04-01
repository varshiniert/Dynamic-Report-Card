package com.evaluate.project.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Term {
    private String term;
    private String subject;
    private double weightedScore;
    
}
