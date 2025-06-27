package com.codemeet.utils.dto.chat;

import com.codemeet.entity.Message;
import com.codemeet.entity.PeerChat;
import com.codemeet.utils.dto.user.UserInfoResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record PeerMessageResponse(
    @NotNull
    Integer messageId,
    
    @NotNull
    Integer chatId,
    
    @NotNull
    Integer peerId,
    
    @NotNull
    UserInfoResponse sender,
    
    @NotNull
    @NotBlank
    String content,
    
    @NotNull
    Instant sentAt
) {
    public static PeerMessageResponse of(Message message) {
        if (message == null) return null;
        return new PeerMessageResponse(
            message.getId(),
            message.getChat().getId(),
            ((PeerChat) message.getChat()).getPeer().getId(),
            UserInfoResponse.of(message.getSender()),
            message.getContent(),
            message.getSentAt()
        );
    }
}
