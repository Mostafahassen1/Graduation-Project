package com.codemeet.utils.dto.chat;

import com.codemeet.entity.Message;
import com.codemeet.utils.dto.user.UserInfoResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record MessageInfoResponse(
    @NotNull
    Integer chatId,
    
    @NotNull
    UserInfoResponse sender,
    
    @NotNull
    @NotBlank
    String content,
    
    @NotNull
    Instant sentAt
) {
    
    public static MessageInfoResponse of(Message message) {
        if (message == null) return null;
        return new MessageInfoResponse(
            message.getChat().getId(),
            UserInfoResponse.of(message.getSender()),
            message.getContent(),
            message.getSentAt()
        );
    }
}
