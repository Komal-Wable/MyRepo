package com.example.lms_communication_service.service;

import com.example.lms_communication_service.client.UserClient;
import com.example.lms_communication_service.dto.UserDTO;
import com.example.lms_communication_service.entity.ChatMessage;
import com.example.lms_communication_service.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    //private final ChatRepository repository;
    private final ChatRepository repository;
    private final UserClient userClient;
    public ChatMessage save(ChatMessage message){


        UserDTO user =
                userClient.getUserById(message.getSenderId());

        message.setSenderName(user.getName());
        message.setSenderRole(user.getRole());

        message.setTimestamp(LocalDateTime.now());

        return repository.save(message);
    }

    public List<ChatMessage> history(Long assignmentId){

        return repository
                .findByAssignmentIdOrderByTimestampAsc(
                        assignmentId);
    }
}
