package com.codemeet.utils.dto.chat;

import com.codemeet.entity.PeerChat;
import com.codemeet.utils.dto.user.UserInfoResponse;
import jakarta.validation.constraints.NotNull;

public record PeerChatInfoResponse(
    @NotNull
    Integer chatId,
    
    @NotNull
    UserInfoResponse peer,
    
    @NotNull
    MessageInfoRequest lastSentMessageInfo
) {
    
    public static PeerChatInfoResponse of(PeerChat chat) {
        return new PeerChatInfoResponse(
            chat.getId(),
            UserInfoResponse.of(chat.getPeer()),
            MessageInfoRequest.of(chat.getLastSentMessage())
        );
    }
}
