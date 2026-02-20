package com.example.lms_assignment_service;

import com.example.events.AssignmentDeletedEvent;
import com.example.lms_assignment_service.client.CourseClient;
import com.example.lms_assignment_service.client.SubmissionClient;
import com.example.lms_assignment_service.dto.AssignmentRequestDTO;
import com.example.lms_assignment_service.entity.Assignment;
import com.example.lms_assignment_service.exception.BadRequestException;
import com.example.lms_assignment_service.exception.ResourceNotFoundException;
import com.example.lms_assignment_service.repository.AssignmentRepository;
import com.example.lms_assignment_service.service.AssignmentService;
import com.example.lms_assignment_service.service.FileStorageService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.core.io.Resource;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssignmentServiceTest {

    @Mock
    private AssignmentRepository repo;

    @Mock
    private CourseClient courseClient;

    @Mock
    private FileStorageService fileStorageService;

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Mock
    private SubmissionClient submissionClient;

    @InjectMocks
    private AssignmentService service;

    private AssignmentRequestDTO dto;

    @BeforeEach
    void setup() {

        dto = new AssignmentRequestDTO();
        dto.setTitle("Test Assignment");
        dto.setDescription("desc");
        dto.setCourseId(1L);
        dto.setDueDate(LocalDateTime.now().plusDays(5));
    }



    @Test
    void createAssignment_success() {

        doNothing().when(courseClient).courseExists(1L);

        Assignment saved = new Assignment();
        saved.setId(10L);
        saved.setTitle(dto.getTitle());

        when(repo.save(any())).thenReturn(saved);

        var response = service.createAssignment(dto, 99L);

        assertNotNull(response);
        assertEquals(10L, response.getId());
    }

    @Test
    void createAssignment_shouldThrow_whenCourseMissing() {

        doThrow(new RuntimeException())
                .when(courseClient)
                .courseExists(1L);

        assertThrows(ResourceNotFoundException.class, () ->
                service.createAssignment(dto, 99L));
    }



    @Test
    void uploadInstruction_success() {

        Assignment assignment = new Assignment();
        assignment.setTeacherId(99L);

        when(repo.findById(1L))
                .thenReturn(Optional.of(assignment));

        MockMultipartFile file =
                new MockMultipartFile(
                        "file",
                        "test.pdf",
                        "application/pdf",
                        "data".getBytes());

        when(fileStorageService.storeFile(file))
                .thenReturn("saved.pdf");

        service.uploadInstruction(1L, 99L, file);

        verify(repo).save(assignment);
    }

    @Test
    void uploadInstruction_shouldThrow_whenUnauthorizedTeacher() {

        Assignment assignment = new Assignment();
        assignment.setTeacherId(55L);

        when(repo.findById(1L))
                .thenReturn(Optional.of(assignment));

        MockMultipartFile file =
                new MockMultipartFile(
                        "file",
                        "t.pdf",
                        "application/pdf",
                        "data".getBytes());

        assertThrows(BadRequestException.class, () ->
                service.uploadInstruction(1L, 99L, file));
    }



    @Test
    void downloadInstruction_success() {

        Assignment a = new Assignment();
        a.setInstructionFileName("file.pdf");

        Resource resource = mock(Resource.class);

        when(repo.findById(1L))
                .thenReturn(Optional.of(a));

        when(fileStorageService.downloadFile("file.pdf"))
                .thenReturn(resource);

        Resource result = service.downloadInstruction(1L);

        assertEquals(resource, result);
    }

    @Test
    void downloadInstruction_shouldThrow_whenFileMissing() {

        Assignment a = new Assignment();

        when(repo.findById(1L))
                .thenReturn(Optional.of(a));

        assertThrows(ResourceNotFoundException.class, () ->
                service.downloadInstruction(1L));
    }


    @Test
    void softDelete_shouldSendKafkaEvent() {

        Assignment assignment = new Assignment();
        assignment.setId(1L);
        assignment.setTeacherId(99L);

        when(repo.findByIdAndDeletedFalse(1L))
                .thenReturn(Optional.of(assignment));

        service.softDelete(1L, 99L);

        ArgumentCaptor<AssignmentDeletedEvent> captor =
                ArgumentCaptor.forClass(AssignmentDeletedEvent.class);

        verify(kafkaTemplate)
                .send(eq("assignment-deleted"), captor.capture());

        assertEquals(1L,
                captor.getValue().getAssignmentId());
    }



    @Test
    void downloadInstructionForStudent_shouldThrow_whenNotEnrolled() {

        Assignment a = new Assignment();
        a.setCourseId(1L);

        when(repo.findByIdAndDeletedFalse(1L))
                .thenReturn(Optional.of(a));

        when(courseClient.checkEnrollment(1L, 10L))
                .thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () ->
                service.downloadInstructionForStudent(1L, 10L));
    }



    @Test
    void countPendingAssignments_shouldReturnCorrectCount() {

        when(courseClient.getEnrolledCourseIds(10L))
                .thenReturn(List.of(1L));

        Assignment a1 = new Assignment();
        a1.setId(1L);

        Assignment a2 = new Assignment();
        a2.setId(2L);

        when(repo.findByCourseIdInAndDeletedFalse(List.of(1L)))
                .thenReturn(List.of(a1, a2));

        when(submissionClient.getSubmittedAssignmentIds(10L))
                .thenReturn(List.of(1L)); // already submitted

        long pending =
                service.countPendingAssignments(10L);

        assertEquals(1, pending);
    }
}
