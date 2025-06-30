package com.codemeet.utils.dto.chat;

import com.codemeet.entity.Message;
import com.codemeet.entity.RoomChat;
import com.codemeet.utils.dto.user.UserInfoResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record RoomMessageResponse(
    @NotNull
    Integer messageId,
    
    @NotNull
    Integer chatId,
    
    @NotNull
    Integer roomId,
    
    @NotNull
    UserInfoResponse sender,
    
    @NotNull
    @NotBlank
    String content,
    
    @NotNull
    Instant sentAt
) {
    public static RoomMessageResponse of(Message message) {
        if (message == null) return null;
        return new RoomMessageResponse(
            message.getId(),
            message.getChat().getId(),
            ((RoomChat) message.getChat()).getRoom().getId(),
            UserInfoResponse.of(message.getSender()),
            message.getContent(),
            message.getSentAt()
        );
    }
}
