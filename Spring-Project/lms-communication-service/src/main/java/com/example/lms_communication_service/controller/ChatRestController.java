package com.example.lms_communication_service.controller;

import com.example.lms_communication_service.entity.ChatMessage;
import com.example.lms_communication_service.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class ChatRestController {

    private final ChatService service;

    @GetMapping("/{assignmentId}")
    public List<ChatMessage> history(
            @PathVariable Long assignmentId){

        return service.history(assignmentId);
    }
}
