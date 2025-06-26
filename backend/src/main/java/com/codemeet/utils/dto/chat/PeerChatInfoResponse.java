package com.codemeet.utils.dto.chat;

import com.codemeet.entity.PeerChat;
import com.codemeet.utils.dto.user.UserInfoResponse;
import jakarta.validation.constraints.NotNull;

public record PeerChatInfoResponse(
    @NotNull
    Integer chatId,
    
    @NotNull
    UserInfoResponse peer,
    
    PeerMessageResponse lastSentMessage
) {
    
    public static PeerChatInfoResponse of(PeerChat chat) {
        return new PeerChatInfoResponse(
            chat.getId(),
            UserInfoResponse.of(chat.getPeer()),
            PeerMessageResponse.of(chat.getLastSentMessage())
        );
    }
}
