package com.codemeet.utils.dto.chat;

import com.codemeet.entity.RoomChat;
import com.codemeet.utils.dto.room.RoomInfoResponse;
import jakarta.validation.constraints.NotNull;

public record RoomChatInfoResponse(
    @NotNull
    Integer chatId,
    
    @NotNull
    RoomInfoResponse room,
    
    MessageInfoResponse lastSentMessage
) {
    
    public static RoomChatInfoResponse of(RoomChat chat) {
        return new RoomChatInfoResponse(
            chat.getId(),
            RoomInfoResponse.of(chat.getRoom()),
            MessageInfoResponse.of(chat.getLastSentMessage())
        );
    }
}
