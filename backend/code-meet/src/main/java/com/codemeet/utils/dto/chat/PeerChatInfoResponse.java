package com.codemeet.utils.dto.chat;

import com.codemeet.entity.PeerChat;
import com.codemeet.utils.dto.UserInfoResponse;
import jakarta.validation.constraints.NotNull;

public record PeerChatInfoResponse(
    @NotNull
    Integer chatId,
    
    @NotNull
    UserInfoResponse one,

    @NotNull
    UserInfoResponse theOther,
    
    @NotNull
    MessageInfo lastSentMessageInfo
) {
    
    public static PeerChatInfoResponse of(PeerChat chat) {
        return new PeerChatInfoResponse(
            chat.getId(),
            UserInfoResponse.of(chat.getOne()),
            UserInfoResponse.of(chat.getTheOther()),
            MessageInfo.of(chat.getLastSentMessage())
        );
    }
}
