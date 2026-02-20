package com.example.lms_communication_service.controller;

import com.example.lms_communication_service.entity.ChatMessage;
import com.example.lms_communication_service.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final ChatService service;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/sendMessage")
    public void sendMessage(ChatMessage message){


        ChatMessage saved =
                service.save(message);


        messagingTemplate.convertAndSend(
                "/topic/assignment/" +
                        saved.getAssignmentId(),
                saved
        );
    }
}
