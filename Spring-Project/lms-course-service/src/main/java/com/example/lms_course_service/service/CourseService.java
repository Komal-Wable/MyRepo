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
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepo;
    private final CourseEventProducer producer;
    private final CourseEnrollmentRepository enrollmentRepo;

    public CourseService(CourseRepository courseRepo,CourseEventProducer producer,CourseEnrollmentRepository enrollmentRepo) {
        this.courseRepo = courseRepo;
        this.producer=producer;
        this.enrollmentRepo = enrollmentRepo;

    }


    public List<CourseListResponseDTO> getAllCourses() {

        return courseRepo.findByDeletedFalse()
                .stream()
                .map(course -> {
                    CourseListResponseDTO dto = new CourseListResponseDTO();
                    dto.setId(course.getId());
                    dto.setTitle(course.getTitle());
                    dto.setDescription(course.getDescription());
                    return dto;
                })
                .toList();
    }


    public CourseResponseDTO createCourse(CourseRequestDTO dto, Long teacherId) {

        Course course = new Course();
        course.setTitle(dto.getTitle());
        course.setDescription(dto.getDescription());
        course.setTeacherId(teacherId);

        Course saved = courseRepo.save(course);

        CourseResponseDTO response = new CourseResponseDTO();
        response.setId(saved.getId());
        response.setTitle(saved.getTitle());
        response.setDescription(saved.getDescription());
        response.setTeacherId(saved.getTeacherId());

        return response;
    }


    @Transactional
    public void softDeleteCourse(Long courseId, Long teacherId) {

        Course course = courseRepo
                .findByIdAndDeletedFalse(courseId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Course not found")
                );

        if (!course.getTeacherId().equals(teacherId)) {
            throw new BadRequestException("You are not allowed to delete this course");
        }

        course.setDeleted(true);
        courseRepo.save(course);


        // COURSE_DELETED

        producer.publishCourseDeleted(courseId);
    }

    public CourseResponseDTO getCourseById(Long id) {

        Course course = courseRepo
                .findByIdAndDeletedFalse(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Course not found"));

        return mapToDTO(course);
    }


    private CourseResponseDTO mapToDTO(Course course) {

        return new CourseResponseDTO(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                course.getTeacherId()
        );
    }


    public void enrollStudent(Long courseId, Long studentId) {


        Course course = courseRepo.findByIdAndDeletedFalse(courseId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Course not found"));


        if (enrollmentRepo.existsByCourseIdAndStudentId(courseId, studentId)) {
            throw new ResourceNotFoundException("Student already enrolled");
        }

        CourseEnrollment enrollment = new CourseEnrollment();
        enrollment.setCourseId(courseId);
        enrollment.setStudentId(studentId);
        enrollment.setEnrolledAt(LocalDateTime.now());

        enrollmentRepo.save(enrollment);
    }

    public boolean isStudentEnrolled(Long courseId, Long studentId) {

        return enrollmentRepo
                .existsByCourseIdAndStudentId(courseId, studentId);
    }


    public List<CourseListResponseDTO> getTeacherCourses(Long teacherId) {

        return courseRepo
                .findByTeacherIdAndDeletedFalse(teacherId)
                .stream()
                .map(course -> new CourseListResponseDTO(
                        course.getId(),
                        course.getTitle(),
                        course.getDescription()
                ))
                .toList();
    }


    public long countCourses(Long teacherId){
        return courseRepo.countByTeacherIdAndDeletedFalse(teacherId);
    }

}
