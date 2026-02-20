package com.example.lms_evaluation_service.service;

import com.example.lms_evaluation_service.client.AssignmentClient;
import com.example.lms_evaluation_service.client.CourseClient;
import com.example.lms_evaluation_service.client.UserClient;
import com.example.lms_evaluation_service.dto.*;
import com.example.lms_evaluation_service.entity.Evaluation;
import com.example.lms_evaluation_service.exception.BadRequestException;
import com.example.lms_evaluation_service.exception.ResourceNotFoundException;
import com.example.lms_evaluation_service.repository.EvaluationRepository;
import com.example.lms_evaluation_service.storage.FileStorageService;
import com.example.lms_evaluation_service.util.ScorecardPdfGenerator;
import jakarta.transaction.Transactional;
//import lombok.Value;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EvaluationService {

    @Value("${file.upload-dir}")
    private String uploadDir;


    private final AssignmentClient assignmentClient;
    private final CourseClient courseClient;
    private final UserClient userClient;
    private final EvaluationRepository repo;
    private final FileStorageService fileStorageService;
    private final ScorecardPdfService pdfService;
   // private final SubmissionClient submissionClient;

    public EvaluationService(
            EvaluationRepository repo,
            FileStorageService fileStorageService,
            ScorecardPdfService pdfService,
            AssignmentClient assignmentClient,
            CourseClient courseClient,
            UserClient userClient) {

        this.repo = repo;
        this.fileStorageService = fileStorageService;
        this.pdfService = pdfService;
        this.assignmentClient = assignmentClient;
        this.courseClient = courseClient;
        this.userClient = userClient;

    }



    public void createEvaluation(
            Long submissionId,
            Long assignmentId,
            Long studentId) {


        if(repo.findBySubmissionId(submissionId).isPresent()){
            return;
        }

        Evaluation evaluation = Evaluation.builder()
                .submissionId(submissionId)
                .assignmentId(assignmentId)
                .studentId(studentId)
                .graded(false)
                .deleted(false)
                .build();

        repo.save(evaluation);
    }


    public Evaluation gradeSubmission(
            Long submissionId,
            Integer marks,
            String grade,
            String feedback) {

        Evaluation evaluation =
                repo.findBySubmissionIdAndDeletedFalse(submissionId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Evaluation not found"));

        evaluation.setMarks(marks);
        evaluation.setGrade(grade);
        evaluation.setFeedback(feedback);
        evaluation.setGraded(true);
        evaluation.setGradedAt(LocalDateTime.now());


        String pdfFile =
                pdfService.generateScorecard(evaluation);

        evaluation.setScorecardFileName(pdfFile);

        return repo.save(evaluation);
    }


    public Resource downloadScorecard(Long submissionId) {

        Evaluation evaluation =
                repo.findBySubmissionIdAndDeletedFalse(submissionId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Evaluation not found"));

        if (!evaluation.isGraded()) {
            throw new ResourceNotFoundException("Not graded yet");
        }

        return fileStorageService.downloadFile(
                evaluation.getScorecardFileName());
    }


    public void deleteBySubmission(Long submissionId) {

        repo.findBySubmissionId(submissionId)
                .ifPresent(evaluation -> {

                    evaluation.setDeleted(true);
                    repo.save(evaluation);
                });
    }
    @Transactional
    public void gradeSubmission(Long submissionId,
                                Integer marks,
                                String feedback) {

        Evaluation eval =
                repo.findBySubmissionId(submissionId)
                        .orElse(new Evaluation());

        eval.setMarks(marks);
        eval.setFeedback(feedback);
        eval.setGraded(true);
        eval.setGradedAt(LocalDateTime.now());
    }
    public Evaluation getBySubmissionAndStudent(
            Long submissionId,
            Long studentId) {

        Evaluation eval =
                repo.findBySubmissionId(submissionId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Evaluation not found"));

        if (!eval.getStudentId().equals(studentId)) {
            throw new BadRequestException("Access denied");
        }

        return eval;
    }

    public Resource downloadScorecardForStudent(
            Long submissionId,
            Long studentId) {

        Evaluation evaluation =
                getBySubmissionAndStudent(
                        submissionId,
                        studentId);

        if (!evaluation.isGraded()) {

            throw new ResourceNotFoundException(
                    "Not graded yet");
        }

        return fileStorageService.downloadFile(
                evaluation.getScorecardFileName());
    }



    public EvaluationResponseDTO gradeSubmission(
            Long submissionId,
            EvaluationRequestDTO dto) throws Exception {

        Evaluation evaluation =
                repo.findBySubmissionIdAndDeletedFalse(submissionId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Evaluation not found"));

        String grade = calculateGrade(dto.getMarks());
        evaluation.setMarks(dto.getMarks());
        evaluation.setFeedback(dto.getFeedback());
        evaluation.setGrade(grade);
        evaluation.setGraded(true);
        evaluation.setGradedAt(LocalDateTime.now());


        AssignmentDTO assignment =
                assignmentClient.getAssignment(
                        evaluation.getAssignmentId()
                );

        CourseDTO course =
                courseClient.getCourse(
                        assignment.getCourseId()
                );

        UserDTO student =
                userClient.getUser(
                        evaluation.getStudentId()
                );

        String fileName =
                ScorecardPdfGenerator.generate(
                        student.getName(),
                        assignment.getTitle(),
                        course.getTitle(),
                        dto.getMarks(),
                        dto.getFeedback(),
                        grade,
                        uploadDir
                );

        evaluation.setScorecardFileName(fileName);
        evaluation.setScorecardOriginalName(fileName);



        repo.save(evaluation);

        return mapToDTO(evaluation);
    }


    private EvaluationResponseDTO mapToDTO(Evaluation e) {

        return EvaluationResponseDTO.builder()
                .id(e.getId())
                .submissionId(e.getSubmissionId())
                .studentId(e.getStudentId())
                .assignmentId(e.getAssignmentId())
                .marks(e.getMarks())
                .feedback(e.getFeedback())
                .grade(e.getGrade())
                .graded(e.isGraded())
                .scorecardFileName(e.getScorecardFileName())
                .build();
    }

    private String calculateGrade(int marks) {

        if (marks >= 90) return "A+";
        if (marks >= 75) return "A";
        if (marks >= 60) return "B";
        if (marks >= 40) return "C";

        return "FAIL";
    }


    public List<EvaluationResponseDTO> getEvaluationsForStudent(Long studentId) {

        return repo
                .findByStudentIdAndDeletedFalse(studentId)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    public List<EvaluationResponseStudentDTO> getStudentEvaluations(Long studentId) {

        List<Evaluation> evaluations =
                repo.findByStudentIdAndGradedTrue(studentId);

        return evaluations.stream().map(ev -> {

            EvaluationResponseStudentDTO dto =
                    new EvaluationResponseStudentDTO();

            dto.setId(ev.getId());
            dto.setMarks(ev.getMarks());
            dto.setGrade(ev.getGrade());
            dto.setFeedback(ev.getFeedback());
            dto.setSubmissionId(ev.getSubmissionId());



            AssignmentDTO assignment =
                    assignmentClient.getAssignment(
                            ev.getAssignmentId()
                    );

            dto.setAssignmentName(assignment.getTitle());



            CourseDTO course =
                    courseClient.getCourse(
                            assignment.getCourseId()
                    );

            dto.setCourseName(course.getTitle());

            return dto;

        }).toList();
    }

    public long countPending(Long teacherId){

        return repo
                .countByTeacherIdAndGradedFalse(teacherId);
    }
}
