package com.evaluate.project.controller;

import com.evaluate.project.model.Exam;
import com.evaluate.project.service.ReportCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private ReportCardService reportCardService;

    @PostMapping("/{studentId}/exams")
    public ResponseEntity<Exam> addExam(@PathVariable String studentId, @RequestBody Exam exam) {
    exam.setStudentId(studentId);
    Exam savedExam = reportCardService.addExam(studentId, exam);
    return ResponseEntity.status(HttpStatus.CREATED).body(savedExam);
}


@GetMapping("/{studentId}/report-card")
public ResponseEntity<String> getStructuredReport(@PathVariable String studentId) {
    Map<String, Object> reportMap = reportCardService.computeDetailedReport(studentId);

    StringBuilder output = new StringBuilder();

    for (Map.Entry<String, Object> entry : reportMap.entrySet()) {
        output.append(entry.getKey()).append(":\n");
        output.append(entry.getValue().toString()).append("\n\n");
    }

    return ResponseEntity.ok(output.toString());
}

}
