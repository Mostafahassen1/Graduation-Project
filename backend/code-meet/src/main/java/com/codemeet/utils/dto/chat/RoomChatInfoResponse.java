package com.codemeet.utils.dto.chat;

import com.codemeet.entity.RoomChat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record RoomChatInfoResponse(
    @NotNull
    Integer chatId,
    
    @NotNull
    Integer roomId,
    
    @NotNull
    @NotBlank
    String roomName,
    
    @NotNull
    @NotBlank
    String roomPictureUrl,
    
    @NotNull
    @NotBlank
    String lastSentMessageContent,
    
    @NotBlank
    Instant lastSentMessageTime
) {
    
    public static RoomChatInfoResponse of(RoomChat chat) {
        return new RoomChatInfoResponse(
            chat.getId(),
            chat.getRoom().getId(),
            chat.getRoom().getName(),
            chat.getRoom().getRoomPictureUrl(),
            chat.getLastSentMessage().getContent(),
            chat.getLastSentMessage().getSentAt()
        );
    }
}
