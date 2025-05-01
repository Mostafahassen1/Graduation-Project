package com.codemeet.utils.dto.room;

import com.codemeet.entity.Room;

import java.time.Instant;
import java.time.LocalDateTime;

public record RoomInfoResponse(
    Integer roomId,
    String roomName,
    String roomDescription,
    Integer creatorId,
    LocalDateTime createdAt,
    String roomPictureUrl
) {
    
    public static RoomInfoResponse of(Room room) {
        return new RoomInfoResponse(
            room.getId(),
            room.getName(),
            room.getDescription(),
            room.getCreator().getId(),
            room.getCreatedAt(),
            room.getRoomPictureUrl()
        );
    }
}
