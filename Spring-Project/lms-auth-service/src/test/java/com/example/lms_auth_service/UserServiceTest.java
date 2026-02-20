package com.example.lms_auth_service;

import com.example.lms_auth_service.dto.RegisterRequestDTO;
import com.example.lms_auth_service.entity.Role;
import com.example.lms_auth_service.entity.User;
import com.example.lms_auth_service.exception.BadRequestException;
import com.example.lms_auth_service.repository.UserRepository;

import com.example.lms_auth_service.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private RegisterRequestDTO dto;

    @BeforeEach
    void setup() {

        dto = new RegisterRequestDTO();
        dto.setName("Komal");
        dto.setEmail("komal@gmail.com");
        dto.setPassword("1234");
        dto.setRole("ROLE_STUDENT");
    }


    @Test
    void register_shouldCreateUser() {

        when(userRepo.findByEmail(dto.getEmail()))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode("1234"))
                .thenReturn("encodedPass");

        User savedUser = new User();
        savedUser.setId(1L);

        when(userRepo.save(any(User.class)))
                .thenReturn(savedUser);

        User result = userService.register(dto);

        assertNotNull(result);
        verify(userRepo).save(any());
    }


    @Test
    void register_shouldThrow_whenEmailExists() {

        when(userRepo.findByEmail(dto.getEmail()))
                .thenReturn(Optional.of(new User()));

        assertThrows(BadRequestException.class, () ->
                userService.register(dto));
    }


    @Test
    void register_teacherShouldBeUnapproved() {

        dto.setRole("ROLE_TEACHER");

        when(userRepo.findByEmail(dto.getEmail()))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode(any()))
                .thenReturn("encoded");

        when(userRepo.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        User user = userService.register(dto);

        assertFalse(user.isApproved());
    }


    @Test
    void findByEmail_success() {

        User user = new User();

        when(userRepo.findByEmail("komal@gmail.com"))
                .thenReturn(Optional.of(user));

        User result = userService.findByEmail("komal@gmail.com");

        assertNotNull(result);
    }
}
