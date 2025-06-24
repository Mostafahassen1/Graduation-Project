package com.codemeet.utils.dto.chat;

import com.codemeet.entity.Chat;
import com.codemeet.entity.PeerChat;
import com.codemeet.entity.RoomChat;
import com.codemeet.utils.dto.room.RoomInfoResponse;
import com.codemeet.utils.dto.user.UserInfoResponse;
import jakarta.validation.constraints.NotNull;

public record ChatInfoResponse(
    @NotNull
    Integer chatId,
    
    @NotNull
    Object other,
    
    MessageInfoResponse lastSentMessage,
    
    @NotNull
    boolean isPeerChat
) {
    public static ChatInfoResponse of(Chat chat) {
        return new ChatInfoResponse(
            chat.getId(),
            chat instanceof PeerChat pc ?
                UserInfoResponse.of(pc.getPeer()) :
                RoomInfoResponse.of(((RoomChat) chat).getRoom()),
            MessageInfoResponse.of(chat.getLastSentMessage()),
            chat instanceof PeerChat
        );
    }
}
