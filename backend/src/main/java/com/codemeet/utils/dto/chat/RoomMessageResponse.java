package com.codemeet.utils.dto.chat;

import com.codemeet.entity.Message;
import com.codemeet.entity.RoomChat;
import com.codemeet.utils.dto.user.UserInfoResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

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
    String content
) {
    public static RoomMessageResponse of(Message message) {
        return new RoomMessageResponse(
            message.getId(),
            message.getChat().getId(),
            ((RoomChat) message.getChat()).getRoom().getId(),
            UserInfoResponse.of(message.getSender()),
            message.getContent()
        );
    }
}
