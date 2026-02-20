package com.example.lms_submission_service.controller;

import com.example.lms_submission_service.dto.LinkSubmissionRequestDTO;
import com.example.lms_submission_service.dto.SubmissionResponseDTO;
import com.example.lms_submission_service.dto.SubmissionTrendDTO;
import com.example.lms_submission_service.entity.Submission;
import com.example.lms_submission_service.exception.BadRequestException;
import com.example.lms_submission_service.repository.SubmissionRepository;
import com.example.lms_submission_service.service.SubmissionService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api")
public class SubmissionController {

    private final SubmissionService service;
    private final SubmissionRepository repo;
    public SubmissionController(
            SubmissionService service,SubmissionRepository repo) {

        this.service = service;
        this.repo=repo;
    }

    @PostMapping("/student/submissions")
    public ResponseEntity<SubmissionResponseDTO>
    submitAssignment(

            @RequestParam Long assignmentId,

            @RequestHeader("X-User-Id")
            Long studentId,

            @RequestParam("file")
            MultipartFile file) {

        return ResponseEntity.ok(
                service.submitAssignment(
                        assignmentId,
                        studentId,
                        file
                )
        );
    }


    @GetMapping("/teacher/submissions/assignment/{assignmentId}")
    public ResponseEntity<List<SubmissionResponseDTO>>
    getSubmissionsByAssignment(
            @PathVariable Long assignmentId,
            @RequestHeader("X-User-Role") String role
    ) {

        if (!role.equals("ROLE_TEACHER")) {
            throw new BadRequestException("Access denied");
        }

        return ResponseEntity.ok(
                service.getSubmissionsByAssignment(assignmentId)
        );
    }

//    public ResponseEntity<Resource> downloadSubmission(
//            @PathVariable Long id) {
//
//        Resource file = service.downloadSubmission(id);
//        Submission sub = service.getSubmission(id);
//
////        return ResponseEntity.ok()
////                .header(HttpHeaders.CONTENT_DISPOSITION,
////                        "attachment; filename=\"" +
////                                sub.getOriginalFileName() + "\"")
////                .body(file);
//
//        return ResponseEntity.ok()
//                .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                .header(HttpHeaders.CONTENT_DISPOSITION,
//                        "attachment; filename=\"" +
//                                sub.getOriginalFileName() + "\"")
//                .body(file);
//    }
@GetMapping("/teacher/submissions/{id}/download")
    public ResponseEntity<Resource> downloadSubmission(
            @PathVariable Long id) throws IOException {

        Resource file = service.downloadSubmission(id);
        Submission sub = service.getSubmission(id);

        Path path = Paths.get("YOUR_UPLOAD_FOLDER")
                .resolve(sub.getFileName());

        String contentType =
                Files.probeContentType(path);

        if(contentType == null){
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" +
                                sub.getOriginalFileName() + "\"")
                .body(file);
    }
    @PostMapping("/student/submissions/status")
    public ResponseEntity<Boolean> checkSubmissionStatus(
            @RequestBody Long assignmentId,
            @RequestHeader("X-User-Id") Long studentId
    ) {
        boolean submitted = service.isAlreadySubmitted(assignmentId, studentId);
        return ResponseEntity.ok(submitted);
    }



    @PostMapping("/student/submissions/link")
    public ResponseEntity<SubmissionResponseDTO>
    submitLink(

            @RequestBody LinkSubmissionRequestDTO dto,

            @RequestHeader("X-User-Id")
            Long studentId
    ) {

        return ResponseEntity.ok(
                service.submitLinkAssignment(
                        dto.getAssignmentId(),
                        studentId,
                        dto.getLinkUrl()
                )
        );
    }
    @GetMapping("/internal/submissions/count/{teacherId}")
    public long countStudents(
            @PathVariable Long teacherId){

        return service.countStudents(teacherId);
    }


    @GetMapping("/internal/submissions/trend/{teacherId}")
    public List<SubmissionTrendDTO> trend(
            @PathVariable Long teacherId){

        return service.getTrend(teacherId);
    }
    @GetMapping("/internal/submissions/student/count/{studentId}")
    public long getSubmissionCountStudent(
            @PathVariable Long studentId){

        return service.countStudentSubmissions(studentId);
    }


    @GetMapping("/internal/submissions/student/trend/{studentId}")
    public List<SubmissionTrendDTO> getTrendStudent(
            @PathVariable Long studentId){

        return service.getTrendStudent(studentId);
    }
    @GetMapping("/internal/submissions/student/{studentId}")
    public List<Long> getSubmittedAssignmentIds(
            @PathVariable Long studentId){
        return service.getSubmittedAssignmentIds(studentId);
    }
}
