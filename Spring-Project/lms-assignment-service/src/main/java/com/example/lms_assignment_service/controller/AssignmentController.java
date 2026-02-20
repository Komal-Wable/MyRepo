package com.example.lms_assignment_service.controller;

import com.example.lms_assignment_service.dto.AssignmentDTO;
import com.example.lms_assignment_service.dto.AssignmentListDTO;
import com.example.lms_assignment_service.dto.AssignmentRequestDTO;
import com.example.lms_assignment_service.dto.AssignmentResponseDTO;
import com.example.lms_assignment_service.entity.Assignment;
import com.example.lms_assignment_service.repository.AssignmentRepository;
import com.example.lms_assignment_service.service.AssignmentService;
import jakarta.validation.Valid;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AssignmentController {


    //private final AssignmentRepository repo;
    private final AssignmentService service;
private final AssignmentRepository assignmentRepo;
    public AssignmentController(AssignmentService service,AssignmentRepository assignmentRepo) {
        this.service = service;
        this.assignmentRepo=assignmentRepo;
    }

    @PostMapping("/teacher/assignments")
    public AssignmentResponseDTO createAssignment(
            @Valid @RequestBody AssignmentRequestDTO dto,
            @RequestHeader("X-User-Id") Long teacherId) {

        return service.createAssignment(dto, teacherId);
    }


    @PostMapping("/teacher/assignments/{id}/upload")
    public ResponseEntity<String> uploadInstruction(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long teacherId,
            @RequestParam("file") MultipartFile file) {

        service.uploadInstruction(id, teacherId, file);

        return ResponseEntity.ok("File uploaded");
    }


    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> download(@PathVariable Long id) {

        Resource file = service.downloadInstruction(id);
        Assignment assignment = service.getAssignment(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" +
                                assignment.getInstructionOriginalName() + "\"")
                .body(file);
    }


    @DeleteMapping("/teacher/assignments/{id}")
    public ResponseEntity<String> deleteAssignment(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long teacherId) {

        service.softDelete(id, teacherId);

        return ResponseEntity.ok("Assignment deleted");
    }

    @GetMapping("/teacher/assignments/courses/{courseId}")
    public List<AssignmentListDTO> getAssignmentsByCourse(
            @PathVariable Long courseId,
            @RequestHeader("X-User-Role") String role) {


        if (!role.equals("ROLE_TEACHER")) {
            throw new RuntimeException("Access denied");
        }

        return service.getAssignmentsByCourse(courseId);
    }


    @GetMapping("/student/assignments/{id}/instruction")
    public ResponseEntity<Resource> downloadInstructionForStudent(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long studentId) {

        Resource file =
                service.downloadInstructionForStudent(id, studentId);

        Assignment assignment =
                service.getAssignment(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" +
                                assignment.getInstructionOriginalName() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
                .body(file);

    }



    @GetMapping("/student/assignments/course/{courseId}")
    public List<AssignmentListDTO> getAssignmentsForStudent(
            @PathVariable Long courseId,
            @RequestHeader("X-User-Id") Long studentId) {

        return service.getAssignmentsForStudent(courseId, studentId);
    }


    //@GetMapping("/internal/assignments/{id}")
    public ResponseEntity<Void> validateAssignment(
            @PathVariable Long id) {

        service.getAssignment(id); // throws if not found

        return ResponseEntity.ok().build();
    }
    @GetMapping("/internal/assignments/{id}")
    public AssignmentDTO getAssignment(@PathVariable Long id) {

        Assignment a = service.getAssignment(id);

        return new AssignmentDTO(
                a.getId(),
                a.getTitle(),
                a.getCourseId(),
                a.getDueDate(),
                a.getTeacherId(),
                a.getAssignmentType().name()
        );
    }
    @GetMapping("/internal/assignments/count/{teacherId}")
    public long countAssignments(@PathVariable Long teacherId){

        return assignmentRepo.countByTeacherId(teacherId);
    }

    @GetMapping("/internal/assignments/student/pending/{studentId}")
    public long pendingAssignments(
            @PathVariable Long studentId){

        return service.countPendingAssignments(studentId);
    }


    @GetMapping("/teacher/assignments/{id}")
    public AssignmentDTO getAssignmentForUser(
            @PathVariable Long id){

        Assignment a = service.getAssignment(id);

        return new AssignmentDTO(
                a.getId(),
                a.getTitle(),
                a.getCourseId(),
                a.getDueDate(),
                a.getTeacherId(),
                a.getAssignmentType().name()
        );
    }

}
