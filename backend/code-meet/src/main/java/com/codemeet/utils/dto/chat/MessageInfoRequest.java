package com.codemeet.utils.dto.chat;

import com.codemeet.entity.Message;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record MessageInfoRequest(
    @NotNull
    Integer chatId,
    
    @NotNull
    Integer senderId,
    
    @NotNull
    @NotBlank
    String content
) {
    
    public static MessageInfoRequest of(Message message) {
        return new MessageInfoRequest(
            message.getChat().getId(),
            message.getSender().getId(),
            message.getContent()
        );
    }
}
