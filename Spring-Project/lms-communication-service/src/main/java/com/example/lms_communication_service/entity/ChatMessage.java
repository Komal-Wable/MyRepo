package com.example.lms_communication_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "sender_name")
    private String senderName;
    private Long assignmentId;

    private Long senderId;

    private String senderRole;

    @Column(length = 2000)
    private String content;

    private LocalDateTime timestamp;
}
