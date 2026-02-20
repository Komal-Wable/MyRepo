package com.example.lms_submission_service;

import com.example.events.SubmissionCreatedEvent;
import com.example.lms_submission_service.client.AssignmentClient;
import com.example.lms_submission_service.client.EvaluationClient;
import com.example.lms_submission_service.client.UserClient;
import com.example.lms_submission_service.dto.AssignmentDTO;
import com.example.lms_submission_service.dto.SubmissionTrendDTO;
import com.example.lms_submission_service.dto.UserDTO;
import com.example.lms_submission_service.entity.Submission;
import com.example.lms_submission_service.entity.SubmissionType;
import com.example.lms_submission_service.exception.ResourceNotFoundException;
import com.example.lms_submission_service.kafka.SubmissionEventProducer;
import com.example.lms_submission_service.repository.SubmissionRepository;
import com.example.lms_submission_service.service.FileStorageService;
import com.example.lms_submission_service.service.SubmissionService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubmissionServiceTest {

    @Mock private SubmissionRepository repo;
    @Mock private AssignmentClient assignmentClient;
    @Mock private FileStorageService fileStorageService;
    @Mock private SubmissionEventProducer producer;
    @Mock private UserClient userClient;
    @Mock private EvaluationClient evaluationClient;

    @InjectMocks
    private SubmissionService service;

    private MockMultipartFile file;
    private AssignmentDTO assignment;

    @BeforeEach
    void setup() {

        file = new MockMultipartFile(
                "file",
                "test.pdf",
                "application/pdf",
                "data".getBytes());

        assignment = new AssignmentDTO();
        assignment.setTeacherId(50L);
        assignment.setAssignmentType("FILE");
        assignment.setDueDate(LocalDateTime.now().plusDays(2));
    }



    @Test
    void submitAssignment_success() {

        when(assignmentClient.getAssignment(1L))
                .thenReturn(assignment);

        doNothing()
                .when(assignmentClient)
                .validateAssignment(1L);

        when(repo.existsByAssignmentIdAndStudentIdAndDeletedFalse(1L,10L))
                .thenReturn(false);

        when(fileStorageService.storeFile(file))
                .thenReturn("stored.pdf");

        Submission saved = new Submission();
        saved.setId(100L);

        when(repo.save(any())).thenReturn(saved);

        var response =
                service.submitAssignment(1L,10L,file);

        assertEquals(100L, response.getId());


        ArgumentCaptor<SubmissionCreatedEvent> captor =
                ArgumentCaptor.forClass(SubmissionCreatedEvent.class);

        verify(producer)
                .publishSubmissionCreated(captor.capture());

        assertEquals(1L,
                captor.getValue().getAssignmentId());
    }



    @Test
    void submitAssignment_shouldFail_whenDueDatePassed() {

        assignment.setDueDate(LocalDateTime.now().minusDays(1));

        when(assignmentClient.getAssignment(1L))
                .thenReturn(assignment);

        assertThrows(ResponseStatusException.class, () ->
                service.submitAssignment(1L,10L,file));
    }



    @Test
    void submitAssignment_shouldFail_whenDuplicate() {

        when(assignmentClient.getAssignment(1L))
                .thenReturn(assignment);

        when(repo.existsByAssignmentIdAndStudentIdAndDeletedFalse(1L,10L))
                .thenReturn(true);

        assertThrows(ResourceNotFoundException.class, () ->
                service.submitAssignment(1L,10L,file));
    }



    @Test
    void submitLinkAssignment_success() {

        when(assignmentClient.getAssignment(1L))
                .thenReturn(assignment);

        when(repo.existsByAssignmentIdAndStudentIdAndDeletedFalse(1L,10L))
                .thenReturn(false);

        Submission saved = new Submission();
        saved.setId(55L);

        when(repo.save(any())).thenReturn(saved);

        var response =
                service.submitLinkAssignment(1L,10L,"github");

        assertEquals(55L, response.getId());

        verify(producer)
                .publishSubmissionCreated(any());
    }



    @Test
    void downloadSubmission_success() {

        Submission sub = new Submission();
        sub.setId(1L);
        sub.setFileName("stored.pdf");

        when(repo.findById(1L))
                .thenReturn(Optional.of(sub));

        Resource resource = mock(Resource.class);

        when(fileStorageService.downloadFile("stored.pdf"))
                .thenReturn(resource);

        assertEquals(resource,
                service.downloadSubmission(1L));
    }



    @Test
    void getSubmissionsByAssignment_shouldMapUserAndGrade() {

        Submission sub = new Submission();
        sub.setId(1L);
        sub.setStudentId(10L);
        sub.setAssignmentId(5L);
        sub.setSubmissionType(SubmissionType.FILE);
        sub.setSubmittedAt(LocalDateTime.now());

        when(repo.findByAssignmentId(5L))
                .thenReturn(List.of(sub));

        UserDTO user = new UserDTO();
        user.setName("Komal");

        when(userClient.getUser(10L))
                .thenReturn(user);

        when(evaluationClient.isGraded(1L))
                .thenReturn(true);

        var result =
                service.getSubmissionsByAssignment(5L);

        assertEquals("Komal",
                result.get(0).getStudentName());


        assertTrue(result.get(0).isGraded());
    }



    @Test
    void getSubmission_shouldThrow_whenMissing() {

        when(repo.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                service.getSubmission(1L));
    }


    @Test
    void countStudents_shouldReturnValue() {

        when(repo.countDistinctStudents(99L))
                .thenReturn(5L);

        assertEquals(5,
                service.countStudents(99L));
    }


}
