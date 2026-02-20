package com.example.lms_assignment_service;

import com.example.lms_assignment_service.controller.AssignmentController;
import com.example.lms_assignment_service.dto.*;
import com.example.lms_assignment_service.entity.AssignmentType;
import com.example.lms_assignment_service.exception.GlobalExceptionHandler;
import com.example.lms_assignment_service.repository.AssignmentRepository;
import com.example.lms_assignment_service.service.AssignmentService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AssignmentController.class)
@Import(GlobalExceptionHandler.class)
class AssignmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;


    @MockitoBean
    private AssignmentService service;

    @MockitoBean
    private AssignmentRepository repository;




    @Test
    void createAssignment_shouldReturn200_whenValidInput() throws Exception {

        AssignmentRequestDTO dto = new AssignmentRequestDTO();
        dto.setTitle("Spring Boot Assignment");
        dto.setDescription("Build REST APIs");
        dto.setCourseId(1L);
        dto.setDueDate(LocalDateTime.now().plusDays(5));

        AssignmentResponseDTO response =
                AssignmentResponseDTO.builder()
                        .id(1L)
                        .title("Spring Boot Assignment")
                        .build();

        when(service.createAssignment(any(), eq(99L)))
                .thenReturn(response);

        mockMvc.perform(post("/api/teacher/assignments")
                        .header("X-User-Id", "99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }









    @Test
    void deleteAssignment_shouldReturn200() throws Exception {

        mockMvc.perform(delete("/api/teacher/assignments/1")
                        .header("X-User-Id", "99"))
                .andExpect(status().isOk());
    }




    @Test
    void getAssignmentsByCourse_shouldReturnList_whenTeacher() throws Exception {

        AssignmentListDTO dto =
                new AssignmentListDTO(
                        1L,
                        "Spring Assignment",
                        "Build APIs",
                        AssignmentType.FILE,
                        LocalDateTime.now().plusDays(5),
                        "instruction.pdf",
                        "instruction.pdf",
                        "Spring Boot Course"
                );

        when(service.getAssignmentsByCourse(1L))
                .thenReturn(List.of(dto));

        mockMvc.perform(get("/api/teacher/assignments/courses/1")
                        .header("X-User-Role", "ROLE_TEACHER"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title")
                        .value("Spring Assignment"));
    }




    @Test
    void countAssignments_shouldReturnCount() throws Exception {

        when(repository.countByTeacherId(99L))
                .thenReturn(5L);

        mockMvc.perform(get("/api/internal/assignments/count/99"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }




    @Test
    void pendingAssignments_shouldReturnCount() throws Exception {

        when(service.countPendingAssignments(10L))
                .thenReturn(3L);

        mockMvc.perform(get("/api/internal/assignments/student/pending/10"))
                .andExpect(status().isOk());
    }
}
