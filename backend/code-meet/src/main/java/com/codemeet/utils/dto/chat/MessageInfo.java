package com.codemeet.utils.dto.chat;

import com.codemeet.entity.Message;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record MessageInfo(
    @NotNull
    Integer chatId,
    
    @NotNull
    Integer senderId,
    
    @NotNull
    @NotBlank
    String content,
    
    @NotNull
    Instant sentAt
) {
    
    public static MessageInfo of(Message message) {
        return new MessageInfo(
            message.getChat().getId(),
            message.getSender().getId(),
            message.getContent(),
            message.getSentAt()
        );
    }
}
