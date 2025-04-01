package com.evaluate.project.service;

import com.evaluate.project.model.Exam;
import com.evaluate.project.repository.ExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReportCardService {

    @Autowired
    private ExamRepository examRepository;

    public Exam addExam(String studentId, Exam exam) {
        exam.setStudentId(studentId);
        if (exam.getExamId() != null) {
            exam.setTerm(mapExamIdToTerm(exam.getExamId()));
        }
        return examRepository.save(exam);
    }

    private String mapExamIdToTerm(String examId) {
        try {
            int id = Integer.parseInt(examId);
            if (id >= 1 && id <= 3) return "1";
            if (id >= 4 && id <= 6) return "2";
        } catch (NumberFormatException ignored) {}
        return "Unknown";
    }

    public Map<String, Object> computeDetailedReport(String studentId) {
        List<Exam> exams = examRepository.findByStudentId(studentId);

        Map<String, Object> report = new LinkedHashMap<>();
        Map<String, Map<String, List<Exam>>> grouped = new TreeMap<>();

        // Group by term -> examId -> list of exams
        for (Exam exam : exams) {
            String term = exam.getTerm();
            String examId = exam.getExamId();

            grouped.computeIfAbsent(term, k -> new TreeMap<>());
            grouped.get(term).computeIfAbsent(examId, k -> new ArrayList<>());
            grouped.get(term).get(examId).add(exam);
        }

        List<Double> allScience = new ArrayList<>();
        List<Double> allEnglish = new ArrayList<>();

        for (String term : grouped.keySet()) {
            StringBuilder termBlock = new StringBuilder();
            termBlock.append("Term: ").append(term).append("\n");

            double termSciTotal = 0;
            double termEngTotal = 0;
            int sciCount = 0;
            int engCount = 0;

            Map<String, List<Exam>> examGroup = grouped.get(term);

            for (String examId : examGroup.keySet()) {
                double sciScore = 0;
                double engScore = 0;

                termBlock.append("Exam: ").append(examId).append("\n");

                for (Exam e : examGroup.get(examId)) {
                    String subj = e.getSubject();
                    double score = e.getExam3();

                    switch (subj) {
                        case "Physics":
                            termBlock.append("Physics: ").append(score).append("\n");
                            sciScore += 0.4 * score;
                            break;
                        case "Chemistry":
                            termBlock.append("Chemistry: ").append(score).append("\n");
                            sciScore += 0.3 * score;
                            break;
                        case "Biology":
                            termBlock.append("Biology: ").append(score).append("\n");
                            sciScore += 0.3 * score;
                            break;
                        case "English":
                            termBlock.append("English: ").append(score).append("\n");
                            double engWeighted = 0.1 * e.getExam1() + 0.1 * e.getExam2() + 0.8 * e.getExam3();
                            engScore = engWeighted;
                            termBlock.append("Total English: ").append(Math.round(engWeighted * 100.0) / 100.0).append("\n");
                            break;
                    }
                }

                if (sciScore > 0) {
                    sciScore = Math.round(sciScore * 100.0) / 100.0;
                    termBlock.append("Total Science: ").append(sciScore).append("\n");
                    termSciTotal += sciScore;
                    sciCount++;
                }

                if (engScore > 0) {
                    termEngTotal += engScore;
                    engCount++;
                }

                termBlock.append("\n");
            }

            // Summary for Term
            termBlock.append("Summary of Term ").append(term).append(":\n");
            if (sciCount > 0) {
                double avgSci = Math.round((termSciTotal / sciCount) * 100.0) / 100.0;
                termBlock.append("Science Score: ").append(avgSci).append("\n");
                allScience.add(avgSci);
            }
            if (engCount > 0) {
                double avgEng = Math.round((termEngTotal / engCount) * 100.0) / 100.0;
                termBlock.append("English Score: ").append(avgEng).append("\n");
                allEnglish.add(avgEng);
            }

            termBlock.append("\n");
            report.put("Term " + term + " Report", termBlock.toString());
        }

        // Final Summary
        double finalSci = allScience.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double finalEng = allEnglish.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);

        Map<String, Object> finalSummary = new LinkedHashMap<>();
        finalSummary.put("final science score", Math.round(finalSci * 100.0) / 100.0);
        finalSummary.put("final english score", Math.round(finalEng * 100.0) / 100.0);

        report.put("Final Summary", finalSummary);
        return report;
    }
}
