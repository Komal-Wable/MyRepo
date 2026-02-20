package com.example.lms_auth_service.controller;

import com.example.lms_auth_service.entity.Role;
import com.example.lms_auth_service.entity.User;
import com.example.lms_auth_service.repository.UserRepository;
import com.example.lms_auth_service.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserRepository userRepo;
    private final UserService userService;

    public AdminController(UserService userService,
                           UserRepository userRepo) {
        this.userService = userService;
        this.userRepo=userRepo;
    }


    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }


    @PutMapping("/teachers/{id}/approve")
    public ResponseEntity<String> approveTeacher(
            @PathVariable Long id) {

        userService.approveTeacher(id);

        return ResponseEntity.ok("Teacher approved");
    }


    @PutMapping("/users/{id}/block")
    public ResponseEntity<String> blockUser(
            @PathVariable Long id) {

        userService.blockUser(id);

        return ResponseEntity.ok("User blocked");
    }


    @PutMapping("/users/{id}/unblock")
    public ResponseEntity<String> unblockUser(
            @PathVariable Long id) {

        userService.unblockUser(id);

        return ResponseEntity.ok("User unblocked");
    }


    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(
            @PathVariable Long id) {

        userService.deleteUser(id);

        return ResponseEntity.ok("User deleted");
    }
    @GetMapping("/teachers")
    public List<User> getAllTeachers() {

        return userRepo.findByRole(Role.ROLE_TEACHER);
    }
    @GetMapping("/students")
    public List<User> getAllStudents() {

        return userRepo.findByRole(Role.ROLE_STUDENT);
    }
}
