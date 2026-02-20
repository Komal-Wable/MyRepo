package com.example.lms_submission_service.service;

import com.example.lms_submission_service.client.AssignmentClient;
import com.example.lms_submission_service.client.EvaluationClient;
import com.example.lms_submission_service.client.UserClient;
import com.example.lms_submission_service.dto.AssignmentDTO;
import com.example.lms_submission_service.dto.SubmissionResponseDTO;
import com.example.lms_submission_service.dto.SubmissionTrendDTO;
import com.example.lms_submission_service.dto.UserDTO;
import com.example.lms_submission_service.entity.Submission;
import com.example.events.SubmissionCreatedEvent;
import com.example.lms_submission_service.entity.SubmissionType;
import com.example.lms_submission_service.exception.ResourceNotFoundException;
import com.example.lms_submission_service.kafka.SubmissionEventProducer;
import com.example.lms_submission_service.repository.SubmissionRepository;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SubmissionService {

    private final EvaluationClient evaluationClient;

    private final SubmissionRepository repo;
    private final AssignmentClient assignmentClient;
    private final FileStorageService fileStorageService;
    private final SubmissionEventProducer producer;
    private final UserClient userClient;
    public SubmissionService(
            SubmissionRepository repo,
            AssignmentClient assignmentClient,
            FileStorageService fileStorageService,
            SubmissionEventProducer producer,UserClient userClient
    ,EvaluationClient evaluationClient) {

        this.repo = repo;
        this.assignmentClient = assignmentClient;
        this.fileStorageService = fileStorageService;
        this.producer = producer;
        this.userClient=userClient;
        this.evaluationClient=evaluationClient;
    }

    public SubmissionResponseDTO submitAssignment(
            Long assignmentId,
            Long studentId,
            MultipartFile file) {

        AssignmentDTO assignment =
                assignmentClient.getAssignment(assignmentId);


        if (assignment.getDueDate()
                .isBefore(LocalDateTime.now())) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Submission closed. Due date has passed."
            );
        }

        Long teacherId = assignment.getTeacherId();


        assignmentClient.validateAssignment(assignmentId);


        if (repo.existsByAssignmentIdAndStudentIdAndDeletedFalse(
                assignmentId,
                studentId)) {

            throw new ResourceNotFoundException(
                    "Assignment already submitted!");
        }

        SubmissionType type =
                SubmissionType.valueOf(
                        assignment.getAssignmentType()
                );




        String fileName =
                fileStorageService.storeFile(file);

        Submission submission = new Submission();

        submission.setAssignmentId(assignmentId);
        submission.setStudentId(studentId);
        submission.setFileName(fileName);
        submission.setTeacherId(teacherId);
        submission.setOriginalFileName(
                file.getOriginalFilename());
        submission.setSubmittedAt(LocalDateTime.now());
        submission.setSubmissionType(type);
        Submission saved = repo.save(submission);


        producer.publishSubmissionCreated(
                new SubmissionCreatedEvent(
                        saved.getId(),
                        assignmentId,
                        studentId,
                        teacherId
                )
        );

        return SubmissionResponseDTO.builder()
                .id(saved.getId())
                .assignmentId(assignmentId)
                .studentId(studentId)
                .fileName(fileName)
                .build();
    }


    public List<SubmissionResponseDTO>
    getSubmissionsByAssignment(Long assignmentId) {

        return repo.findByAssignmentId(assignmentId)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }
    public Resource downloadSubmission(Long submissionId) {

        Submission submission = getSubmission(submissionId);

        return fileStorageService.downloadFile(
                submission.getFileName()
        );
    }


    public Submission getSubmission(Long id) {

        return repo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Submission not found"));
    }


//    private SubmissionResponseDTO mapToDTO(
//            Submission submission) {
//
//        return new SubmissionResponseDTO(
//                submission.getId(),
//                submission.getStudentId(),
//                submission.getOriginalFileName(),
//                submission.getSubmittedAt()
//        );
//    }
    private SubmissionResponseDTO mapToDTO(
            Submission submission) {

        UserDTO user =
                userClient.getUser(
                        submission.getStudentId());

        Boolean graded =
                evaluationClient.isGraded(
                        submission.getId());
        return SubmissionResponseDTO.builder()
                .id(submission.getId())
                .assignmentId(submission.getAssignmentId())
                .studentId(submission.getStudentId())
                .studentName(user.getName())
                .submittedAt(submission.getSubmittedAt())
                .graded(graded)
                .submissionType(
                        submission.getSubmissionType().name()
                )
                .linkUrl(submission.getLinkUrl())
                .fileName(submission.getOriginalFileName())
                .build();

    }

    public boolean isAlreadySubmitted(
            Long assignmentId,
            Long studentId) {

        return repo.existsByAssignmentIdAndStudentIdAndDeletedFalse(
                assignmentId,
                studentId
        );
    }
    public SubmissionResponseDTO submitLinkAssignment(

            Long assignmentId,
            Long studentId,
            String linkUrl) {

        AssignmentDTO assignment =
                assignmentClient.getAssignment(assignmentId);
        Long teacherId = assignment.getTeacherId();

        if (assignment.getDueDate()
                .isBefore(LocalDateTime.now())) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Submission closed. Due date passed."
            );
        }


        if (repo.existsByAssignmentIdAndStudentIdAndDeletedFalse(
                assignmentId,
                studentId)) {

            throw new ResourceNotFoundException(
                    "Assignment already submitted!");
        }

        Submission submission = new Submission();

        submission.setAssignmentId(assignmentId);
        submission.setStudentId(studentId);
        submission.setSubmissionType(SubmissionType.LINK);
        submission.setLinkUrl(linkUrl);
        submission.setSubmittedAt(LocalDateTime.now());
        submission.setTeacherId(teacherId);
        Submission saved = repo.save(submission);


        producer.publishSubmissionCreated(
                new SubmissionCreatedEvent(
                        saved.getId(),
                        assignmentId,
                        studentId,
                        teacherId
                )
        );

        return SubmissionResponseDTO.builder()
                .id(saved.getId())
                .assignmentId(assignmentId)
                .studentId(studentId)
                .build();
    }


    public long countStudents(Long teacherId){

        return repo
                .countDistinctStudents(teacherId);
    }


    public List<SubmissionTrendDTO> getTrend(Long teacherId){

        return repo.getSubmissionTrend(teacherId)
                .stream()
                .map(obj -> new SubmissionTrendDTO(
                        obj[0].toString(),
                        (Long)obj[1]
                ))
                .toList();
    }


    public List<SubmissionTrendDTO> getTrendStudent(Long studentId){

        return repo.submissionTrend(studentId)
                .stream()
                .map(obj -> new SubmissionTrendDTO(
                        obj[0].toString(),
                        (Long)obj[1]
                ))
                .toList();
    }


    public List<Long> getSubmittedAssignmentIds(Long studentId){

        return repo
                .findAssignmentIdsByStudentId(studentId);
    }
    public long countStudentSubmissions(Long studentId){
        return repo.countStudentSubmissions(studentId);
    }

}
