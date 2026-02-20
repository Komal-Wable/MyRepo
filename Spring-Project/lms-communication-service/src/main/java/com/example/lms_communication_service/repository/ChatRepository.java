package com.example.lms_communication_service.repository;

import com.example.lms_communication_service.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository
        extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage>
    findByAssignmentIdOrderByTimestampAsc(
            Long assignmentId);
}
