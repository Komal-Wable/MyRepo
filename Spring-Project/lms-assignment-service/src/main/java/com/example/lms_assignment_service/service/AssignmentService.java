package com.example.lms_assignment_service.service;

import com.example.events.AssignmentDeletedEvent;
import com.example.lms_assignment_service.client.CourseClient;
import com.example.lms_assignment_service.client.SubmissionClient;
import com.example.lms_assignment_service.dto.AssignmentListDTO;
import com.example.lms_assignment_service.dto.AssignmentRequestDTO;
import com.example.lms_assignment_service.dto.AssignmentResponseDTO;
import com.example.lms_assignment_service.entity.Assignment;
import com.example.lms_assignment_service.exception.BadRequestException;
import com.example.lms_assignment_service.exception.ResourceNotFoundException;
import com.example.lms_assignment_service.kafka.AssignmentEventProducer;
import com.example.lms_assignment_service.repository.AssignmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.core.io.Resource;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AssignmentService {

    private final SubmissionClient submissionClient;
    private final AssignmentRepository repo;
    private final CourseClient courseClient;
    private final FileStorageService fileStorageService;
    private final AssignmentEventProducer producer;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    // Constructor Injection (BEST PRACTICE)
    public AssignmentService(AssignmentRepository repo,
                             CourseClient courseClient,
                             FileStorageService fileStorageService,
                             AssignmentEventProducer producer,
                             KafkaTemplate<String, Object> kafkaTemplate,
                             SubmissionClient submissionClient) {

        this.repo = repo;
        this.courseClient = courseClient;
        this.fileStorageService = fileStorageService;
        this.producer = producer;
        this.kafkaTemplate=kafkaTemplate;
        this.submissionClient=submissionClient;
    }



    public AssignmentResponseDTO createAssignment(
            AssignmentRequestDTO dto,
            Long teacherId) {


        try {
            courseClient.courseExists(dto.getCourseId());
        } catch (Exception e) {
            throw new ResourceNotFoundException("Course not found");
        }


        Assignment assignment = new Assignment();

        assignment.setTitle(dto.getTitle());
        assignment.setDescription(dto.getDescription());
        assignment.setAssignmentType(dto.getAssignmentType());

        // LINK assignments
        assignment.setInstructionPathLink(dto.getInstructionPathLink());

        assignment.setDueDate(dto.getDueDate());
        assignment.setCourseId(dto.getCourseId());
        assignment.setTeacherId(teacherId);
        assignment.setCreatedAt(LocalDateTime.now());
        //assignment.setInstructionFileName(savedFileName);
        //assignment.setInstructionOriginalName(file.getOriginalFilename());

        Assignment saved = repo.save(assignment);

        return AssignmentResponseDTO.builder()
                .id(saved.getId())
                .title(saved.getTitle())
                .description(saved.getDescription())
                .assignmentType(saved.getAssignmentType())
                .instructionPathLink(saved.getInstructionPathLink())
                .dueDate(saved.getDueDate())
                .courseId(saved.getCourseId())
                .build();
    }


    //UPLOAD INSTRUCTION FILE
    public void uploadInstruction(Long assignmentId,
                                  Long teacherId,
                                  MultipartFile file) {

        Assignment assignment = repo.findById(assignmentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Assignment not found"));

        //  Teacher Ownership Check (VERY IMPORTANT)
        if (!assignment.getTeacherId().equals(teacherId)) {
            throw new BadRequestException("Unauthorized");
        }

        String savedFileName = fileStorageService.storeFile(file);

        assignment.setInstructionFileName(savedFileName);
        assignment.setInstructionOriginalName(file.getOriginalFilename());

        repo.save(assignment);

    }



    public Resource downloadInstruction(Long assignmentId) {

        Assignment assignment = repo.findById(assignmentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Assignment not found"));

        if (assignment.getInstructionFileName() == null) {
            throw new ResourceNotFoundException("No file uploaded for this assignment");
        }

        return fileStorageService.downloadFile(
                assignment.getInstructionFileName());
    }


    @Transactional
    public void softDelete(Long assignmentId, Long teacherId) {

        Assignment assignment = repo.findByIdAndDeletedFalse(assignmentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Assignment not found"));

        if (!assignment.getTeacherId().equals(teacherId)) {
            throw new BadRequestException("Unauthorized");
        }

        assignment.setDeleted(true);
        repo.save(assignment);

        kafkaTemplate.send(
                "assignment-deleted",
                new AssignmentDeletedEvent(
                        assignment.getId()
                )
        );

        System.out.println(
                " AssignmentDeletedEvent published → "
                        + assignment.getId()
        );
      //  producer.publishAssignmentDeleted(assignmentId);
    }

    @Transactional
    public void deleteAssignmentsByCourse(Long courseId) {

        List<Assignment> assignments =
                repo.findByCourseIdAndDeletedFalse(courseId);

        for (Assignment assignment : assignments) {

            assignment.setDeleted(true);

            kafkaTemplate.send(
                    "assignment-deleted",
                    new AssignmentDeletedEvent(
                            assignment.getId()
                    )
            );

            System.out.println(
                    "AssignmentDeletedEvent published → "
                            + assignment.getId()
            );
        }
    }



    public Assignment getAssignment(Long assignmentId) {

        return repo.findByIdAndDeletedFalse(assignmentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Assignment not found"));
    }
    public List<AssignmentListDTO> getAssignmentsByCourse(Long courseId) {

        List<Assignment> assignments =
                repo.findByCourseIdAndDeletedFalse(courseId);

        return assignments.stream()
                .map(a -> AssignmentListDTO.builder()
                        .id(a.getId())
                        .title(a.getTitle())
                        .description(a.getDescription())
                        .assignmentType(a.getAssignmentType())
                        .dueDate(a.getDueDate())
                        .instructionFileName(a.getInstructionFileName())
                        .instructionOriginalName(a.getInstructionOriginalName())
                        .instructionPathLink(a.getInstructionPathLink())
                        .build())
                .toList();
    }

    public Resource downloadInstructionForStudent(
            Long assignmentId,
            Long studentId) {

        Assignment assignment =
                repo.findByIdAndDeletedFalse(assignmentId)
                        .orElseThrow(() ->
                                new RuntimeException("Assignment not found"));


        boolean enrolled =
                courseClient.checkEnrollment(
                        assignment.getCourseId(),
                        studentId);

        if (!enrolled) {
            throw new ResourceNotFoundException(
                    "You are not enrolled in this course");
        }

        if (assignment.getInstructionFileName() == null) {
            throw new ResourceNotFoundException("No instruction file");
        }

        return fileStorageService.downloadFile(
                assignment.getInstructionFileName());
    }


    public List<AssignmentListDTO> getAssignmentsForStudent(
            Long courseId,
            Long studentId) {


        Boolean enrolled =
                courseClient.checkEnrollment(courseId, studentId);

        if (!enrolled) {
            throw new RuntimeException(
                    "You are not enrolled in this course");
        }

        return repo
                .findByCourseIdAndDeletedFalse(courseId)
                .stream()
                .map(a -> AssignmentListDTO.builder()
                        .id(a.getId())
                        .title(a.getTitle())
                        .description(a.getDescription())
                        .assignmentType(a.getAssignmentType())
                        .dueDate(a.getDueDate())
                        .instructionFileName(a.getInstructionFileName())
                        .instructionOriginalName(a.getInstructionOriginalName())
                        .instructionPathLink(a.getInstructionPathLink())
                        .build())
                .toList();

    }


    public long getPendingAssignments(Long studentId){

        List<Long> courseIds =
                courseClient.getEnrolledCourseIds(studentId);

        if(courseIds.isEmpty())
            return 0;

        return repo
                .countAssignments(courseIds);
    }

    public long countPendingAssignments(Long studentId){

        List<Long> courseIds =
                courseClient.getEnrolledCourseIds(studentId);

        if(courseIds.isEmpty()) return 0;

        List<Assignment> assignments =
                repo.findByCourseIdInAndDeletedFalse(courseIds);

        Set<Long> submitted =
                new HashSet<>(
                        submissionClient.getSubmittedAssignmentIds(studentId)
                );

        return assignments.stream()
                .filter(a -> !submitted.contains(a.getId()))
                .count();
    }


}
