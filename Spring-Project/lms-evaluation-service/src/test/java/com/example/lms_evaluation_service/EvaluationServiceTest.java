package com.example.lms_evaluation_service;

import com.example.lms_evaluation_service.client.AssignmentClient;
import com.example.lms_evaluation_service.client.CourseClient;
import com.example.lms_evaluation_service.client.UserClient;
import com.example.lms_evaluation_service.dto.*;
import com.example.lms_evaluation_service.entity.Evaluation;
import com.example.lms_evaluation_service.exception.BadRequestException;
import com.example.lms_evaluation_service.exception.ResourceNotFoundException;
import com.example.lms_evaluation_service.repository.EvaluationRepository;
import com.example.lms_evaluation_service.service.EvaluationService;
import com.example.lms_evaluation_service.service.ScorecardPdfService;
import com.example.lms_evaluation_service.storage.FileStorageService;
import com.example.lms_evaluation_service.util.ScorecardPdfGenerator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.core.io.Resource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EvaluationServiceTest {

    @Mock private EvaluationRepository repo;
    @Mock private FileStorageService fileStorageService;
    @Mock private ScorecardPdfService pdfService;
    @Mock private AssignmentClient assignmentClient;
    @Mock private CourseClient courseClient;
    @Mock private UserClient userClient;

    @InjectMocks
    private EvaluationService service;

    private Evaluation evaluation;

    @BeforeEach
    void setup() {

        evaluation = Evaluation.builder()
                .id(1L)
                .submissionId(10L)
                .assignmentId(20L)
                .studentId(30L)
                .graded(false)
                .deleted(false)
                .build();
    }



    @Test
    void createEvaluation_shouldSave_whenNotExists() {

        when(repo.findBySubmissionId(10L))
                .thenReturn(Optional.empty());

        service.createEvaluation(10L,20L,30L);

        verify(repo).save(any(Evaluation.class));
    }

    @Test
    void createEvaluation_shouldSkip_whenDuplicate() {

        when(repo.findBySubmissionId(10L))
                .thenReturn(Optional.of(evaluation));

        service.createEvaluation(10L,20L,30L);

        verify(repo, never()).save(any());
    }



    @Test
    void gradeSubmission_shouldUpdateEvaluation() {

        when(repo.findBySubmissionIdAndDeletedFalse(10L))
                .thenReturn(Optional.of(evaluation));

        when(pdfService.generateScorecard(any()))
                .thenReturn("score.pdf");


        when(repo.save(any(Evaluation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Evaluation saved = service.gradeSubmission(
                10L,90,"A+","Excellent");

        assertTrue(saved.isGraded());
        assertEquals("score.pdf",
                saved.getScorecardFileName());
    }



    @Test
    void downloadScorecard_success() {

        evaluation.setGraded(true);
        evaluation.setScorecardFileName("score.pdf");

        when(repo.findBySubmissionIdAndDeletedFalse(10L))
                .thenReturn(Optional.of(evaluation));

        Resource resource = mock(Resource.class);

        when(fileStorageService.downloadFile("score.pdf"))
                .thenReturn(resource);

        assertEquals(resource,
                service.downloadScorecard(10L));
    }

    @Test
    void downloadScorecard_shouldFail_whenNotGraded() {

        when(repo.findBySubmissionIdAndDeletedFalse(10L))
                .thenReturn(Optional.of(evaluation));

        assertThrows(ResourceNotFoundException.class, () ->
                service.downloadScorecard(10L));
    }


    @Test
    void deleteBySubmission_shouldSoftDelete() {

        when(repo.findBySubmissionId(10L))
                .thenReturn(Optional.of(evaluation));

        service.deleteBySubmission(10L);

        assertTrue(evaluation.isDeleted());

        verify(repo).save(evaluation);
    }


    @Test
    void getBySubmissionAndStudent_shouldFail_whenUnauthorized() {

        evaluation.setStudentId(99L);

        when(repo.findBySubmissionId(10L))
                .thenReturn(Optional.of(evaluation));

        assertThrows(BadRequestException.class, () ->
                service.getBySubmissionAndStudent(10L,30L));
    }


    @Test
    void gradeSubmissionDTO_shouldGeneratePdf() throws Exception {

        evaluation.setStudentId(30L);

        when(repo.findBySubmissionIdAndDeletedFalse(10L))
                .thenReturn(Optional.of(evaluation));

        AssignmentDTO assignment = new AssignmentDTO();
        assignment.setTitle("Spring");
        assignment.setCourseId(5L);

        when(assignmentClient.getAssignment(20L))
                .thenReturn(assignment);

        CourseDTO course = new CourseDTO();
        course.setTitle("Backend");

        when(courseClient.getCourse(5L))
                .thenReturn(course);

        UserDTO user = new UserDTO();
        user.setName("Komal");

        when(userClient.getUser(30L))
                .thenReturn(user);

        EvaluationRequestDTO dto =
                new EvaluationRequestDTO();

        dto.setMarks(95);
        dto.setFeedback("Great");


        try (MockedStatic<ScorecardPdfGenerator> mocked =
                     mockStatic(ScorecardPdfGenerator.class)) {

            mocked.when(() ->
                            ScorecardPdfGenerator.generate(
                                    any(), any(), any(),
                                    anyInt(), any(),
                                    any(), any()))
                    .thenReturn("scorecard.pdf");

            EvaluationResponseDTO response =
                    service.gradeSubmission(10L,dto);

            assertEquals("A+",
                    response.getGrade());

            assertTrue(response.isGraded());
        }
    }



    @Test
    void getStudentEvaluations_shouldMapAssignmentAndCourse() {

        evaluation.setGraded(true);

        when(repo.findByStudentIdAndGradedTrue(30L))
                .thenReturn(List.of(evaluation));

        AssignmentDTO assignment = new AssignmentDTO();
        assignment.setTitle("Spring");
        assignment.setCourseId(5L);

        when(assignmentClient.getAssignment(20L))
                .thenReturn(assignment);

        CourseDTO course = new CourseDTO();
        course.setTitle("Backend");

        when(courseClient.getCourse(5L))
                .thenReturn(course);

        var result =
                service.getStudentEvaluations(30L);

        assertEquals("Spring",
                result.get(0).getAssignmentName());

        assertEquals("Backend",
                result.get(0).getCourseName());
    }



    @Test
    void countPending_shouldReturnValue() {

        when(repo.countByTeacherIdAndGradedFalse(50L))
                .thenReturn(4L);

        assertEquals(4,
                service.countPending(50L));
    }
}
