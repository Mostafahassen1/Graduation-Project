package com.codemeet.utils.dto.chat;

import com.codemeet.entity.Message;
import com.codemeet.utils.dto.user.UserInfoResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.time.LocalDateTime;

public record MessageInfoResponse(
    @NotNull
    UserInfoResponse senderInfo,
    
    @NotNull
    @NotBlank
    String content,
    
    @NotNull
    Instant sentAt
) {
    
    public static MessageInfoResponse of(Message message) {
        return new MessageInfoResponse(
            UserInfoResponse.of(message.getSender()),
            message.getContent(),
            message.getSentAt()
        );
    }
}
