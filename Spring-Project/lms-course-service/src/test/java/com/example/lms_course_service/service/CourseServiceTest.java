package com.example.lms_course_service.service;

import com.example.lms_course_service.dto.CourseListResponseDTO;
import com.example.lms_course_service.dto.CourseRequestDTO;
import com.example.lms_course_service.dto.CourseResponseDTO;
import com.example.lms_course_service.entity.Course;
import com.example.lms_course_service.entity.CourseEnrollment;
import com.example.lms_course_service.exception.BadRequestException;
import com.example.lms_course_service.exception.ResourceNotFoundException;
import com.example.lms_course_service.kafka.CourseEventProducer;
import com.example.lms_course_service.repository.CourseEnrollmentRepository;
import com.example.lms_course_service.repository.CourseRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepo;

    @Mock
    private CourseEventProducer producer;

    @Mock
    private CourseEnrollmentRepository enrollmentRepo;

    @InjectMocks
    private CourseService service;

    private Course course;

    @BeforeEach
    void setup() {

        course = new Course();
        course.setId(1L);
        course.setTitle("Spring Boot");
        course.setDescription("Master Spring");
        course.setTeacherId(99L);
        course.setDeleted(false);
    }


    @Test
    void getAllCourses_shouldReturnList() {

        when(courseRepo.findByDeletedFalse())
                .thenReturn(List.of(course));

        List<CourseListResponseDTO> result =
                service.getAllCourses();

        assertEquals(1, result.size());
        assertEquals("Spring Boot",
                result.get(0).getTitle());
    }


    @Test
    void createCourse_shouldSaveCourse() {

        CourseRequestDTO dto =
                new CourseRequestDTO();

        dto.setTitle("Java");
        dto.setDescription("Core Java");

        when(courseRepo.save(any()))
                .thenReturn(course);

        CourseResponseDTO response =
                service.createCourse(dto, 99L);

        assertNotNull(response);
        assertEquals("Spring Boot",
                response.getTitle());
    }


    @Test
    void softDeleteCourse_shouldPublishEvent() {

        when(courseRepo.findByIdAndDeletedFalse(1L))
                .thenReturn(Optional.of(course));

        service.softDeleteCourse(1L, 99L);

        verify(producer)
                .publishCourseDeleted(1L);

        verify(courseRepo)
                .save(course);
    }


    @Test
    void softDeleteCourse_shouldFail_whenWrongTeacher() {

        when(courseRepo.findByIdAndDeletedFalse(1L))
                .thenReturn(Optional.of(course));

        assertThrows(BadRequestException.class, () ->
                service.softDeleteCourse(1L, 55L));
    }


    @Test
    void softDeleteCourse_shouldThrow_whenNotFound() {

        when(courseRepo.findByIdAndDeletedFalse(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                service.softDeleteCourse(1L, 99L));
    }


    @Test
    void getCourseById_success() {

        when(courseRepo.findByIdAndDeletedFalse(1L))
                .thenReturn(Optional.of(course));

        CourseResponseDTO dto =
                service.getCourseById(1L);

        assertEquals("Spring Boot", dto.getTitle());
    }


    @Test
    void enrollStudent_shouldSaveEnrollment() {

        when(courseRepo.findByIdAndDeletedFalse(1L))
                .thenReturn(Optional.of(course));

        when(enrollmentRepo.existsByCourseIdAndStudentId(1L,10L))
                .thenReturn(false);

        service.enrollStudent(1L,10L);

        verify(enrollmentRepo)
                .save(any(CourseEnrollment.class));
    }


    @Test
    void enrollStudent_shouldFail_whenAlreadyEnrolled() {

        when(courseRepo.findByIdAndDeletedFalse(1L))
                .thenReturn(Optional.of(course));

        when(enrollmentRepo.existsByCourseIdAndStudentId(1L,10L))
                .thenReturn(true);

        assertThrows(RuntimeException.class, () ->
                service.enrollStudent(1L,10L));
    }


    @Test
    void isStudentEnrolled_shouldReturnTrue() {

        when(enrollmentRepo.existsByCourseIdAndStudentId(1L,10L))
                .thenReturn(true);

        assertTrue(
                service.isStudentEnrolled(1L,10L)
        );
    }


    @Test
    void getTeacherCourses_shouldReturnList() {

        when(courseRepo.findByTeacherIdAndDeletedFalse(99L))
                .thenReturn(List.of(course));

        List<CourseListResponseDTO> result =
                service.getTeacherCourses(99L);

        assertEquals(1, result.size());
    }
}
