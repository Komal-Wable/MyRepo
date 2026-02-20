package com.example.lms_auth_service.service;

import com.example.lms_auth_service.dto.RegisterRequestDTO;
import com.example.lms_auth_service.dto.UserProfileDto;
import com.example.lms_auth_service.entity.Role;
import com.example.lms_auth_service.entity.User;
import com.example.lms_auth_service.exception.BadRequestException;
import com.example.lms_auth_service.exception.ResourceNotFoundException;
import com.example.lms_auth_service.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepo,
                       PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }


    public User register(RegisterRequestDTO dto) {

        userRepo.findByEmail(dto.getEmail()).ifPresent(u -> {
            throw new BadRequestException("Email already registered");
        });

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        Role role = dto.getRole() != null
                ? Role.valueOf(dto.getRole())
                : Role.ROLE_STUDENT;

        user.setRole(role);


        if (role == Role.ROLE_TEACHER) {
            user.setApproved(false);
        } else {
            user.setApproved(true);
        }

        return userRepo.save(user);
    }

  
    public User getUserById(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + id));
    }

    public User findByEmail(String email) {
        return userRepo.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
    }



    public UserProfileDto getProfile(String email) {

        User user = userRepo.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        return UserProfileDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }


    public void approveTeacher(Long id) {

        User teacher = getUserById(id);

        if (teacher.getRole() != Role.ROLE_TEACHER) {
            throw new BadRequestException("User is not a teacher");
        }

        teacher.setApproved(true);

        userRepo.save(teacher);
    }



    public void blockUser(Long id) {

        User user = getUserById(id);

        user.setBlocked(true);

        userRepo.save(user);
    }



    public void unblockUser(Long id) {

        User user = getUserById(id);

        user.setBlocked(false);

        userRepo.save(user);
    }



    public void deleteUser(Long id) {

        User user = getUserById(id);

        userRepo.delete(user);
    }
}
