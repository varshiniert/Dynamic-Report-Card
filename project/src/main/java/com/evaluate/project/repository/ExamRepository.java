package com.evaluate.project.repository;

import com.evaluate.project.model.Exam;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ExamRepository extends MongoRepository<Exam, String> {
    List<Exam> findByStudentId(String studentId);
}
