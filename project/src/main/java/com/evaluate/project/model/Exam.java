package com.evaluate.project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("exams")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Exam {
    @Id
    private String id;

    private String studentId;
    private String subject;
    private String examId;
    private String term;
    private Double exam1;
    private Double exam2;
    private Double exam3;
}
