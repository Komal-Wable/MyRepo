package com.example.lms_evaluation_service.controller;

import com.example.lms_evaluation_service.dto.EvaluationRequestDTO;
import com.example.lms_evaluation_service.dto.EvaluationResponseDTO;
import com.example.lms_evaluation_service.dto.EvaluationResponseStudentDTO;
import com.example.lms_evaluation_service.entity.Evaluation;
import com.example.lms_evaluation_service.repository.EvaluationRepository;
import com.example.lms_evaluation_service.service.EvaluationService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EvaluationController {

    private final EvaluationService service;
    private final EvaluationRepository repo;
    public EvaluationController(EvaluationService service,EvaluationRepository repo) {

        this.service = service;
        this.repo=repo;
    }


   // @PostMapping("/teacher/evaluations/{submissionId}")
    public Evaluation grade(
            @PathVariable Long submissionId,
            @RequestParam Integer marks,
            @RequestParam String grade,
            @RequestParam String feedback) {

        return service.gradeSubmission(
                submissionId,
                marks,
                grade,
                feedback);
    }



    public ResponseEntity<Resource> download(
            @PathVariable Long submissionId) {

        Resource file =
                service.downloadScorecard(submissionId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=scorecard.pdf")
                .body(file);
    }
    @PutMapping("/teacher/evaluations/{submissionId}/grade")
    public ResponseEntity<String> gradeSubmission(

            @PathVariable Long submissionId,

            @RequestParam Integer marks,

            @RequestParam String feedback,

            @RequestHeader("X-User-Role") String role
    ) {

        if (!role.equals("ROLE_TEACHER")) {
            throw new RuntimeException("Access denied");
        }

        service.gradeSubmission(
                submissionId,
                marks,
                feedback
        );

        return ResponseEntity.ok("Graded Successfully");
    }
    @GetMapping("/student/evaluations/{submissionId}")
    public Evaluation getEvaluation(
            @PathVariable Long submissionId,
            @RequestHeader("X-User-Id") Long studentId) {

        return service.getBySubmissionAndStudent(
                submissionId,
                studentId
        );
    }


    @GetMapping("/student/evaluations/{submissionId}/scorecard")
    public ResponseEntity<Resource> downloadScorecard(
            @PathVariable Long submissionId,
            @RequestHeader("X-User-Id") Long studentId) {

        Resource file =
                service.downloadScorecardForStudent(
                        submissionId,
                        studentId
                );

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"scorecard.pdf\"")
                .body(file);
    }

    @PostMapping("/teacher/evaluations/{submissionId}/grade")
    public EvaluationResponseDTO grade(
            @PathVariable Long submissionId,
            @RequestBody EvaluationRequestDTO dto
            )
            throws Exception {

        return service.gradeSubmission(submissionId, dto);
    }

//    @GetMapping("/student")
//    public List<EvaluationResponseDTO> getStudentEvaluations(
//            @RequestHeader("X-User-Id") Long studentId) {
//
//        return service.getEvaluationsForStudent(studentId);
//    }





    @GetMapping("/student/evaluations")
    public ResponseEntity<List<EvaluationResponseStudentDTO>>
    getStudentEvaluations(
            @RequestHeader("X-User-Id") Long studentId
    ) {

        return ResponseEntity.ok(
                service.getStudentEvaluations(studentId)
        );
    }


    @GetMapping("/internal/evaluations/pending/{teacherId}")
    public long pendingEvaluations(
            @PathVariable Long teacherId){

        return service.countPending(teacherId);
    }
    @GetMapping("/internal/evaluations/student/count/{studentId}")
    public long countEvaluated(
            @PathVariable Long studentId){

        return repo.countByStudentIdAndGradedTrue(studentId);
    }

}
