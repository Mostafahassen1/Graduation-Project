package com.codemeet.utils.dto.room;

import com.codemeet.entity.Room;
import com.codemeet.utils.dto.user.UserInfoResponse;

import java.time.Instant;

public record RoomInfoResponse(
    Integer roomId,
    String roomName,
    String roomDescription,
    UserInfoResponse creator,
    Instant createdAt,
    String roomPictureUrl
) {
    
    public static RoomInfoResponse of(Room room) {
        return new RoomInfoResponse(
            room.getId(),
            room.getName(),
            room.getDescription(),
            UserInfoResponse.of(room.getCreator()),
            room.getCreatedAt(),
            room.getRoomPictureUrl()
        );
    }
}
