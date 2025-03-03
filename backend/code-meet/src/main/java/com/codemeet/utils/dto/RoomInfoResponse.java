package com.codemeet.utils.dto;

import com.codemeet.entity.Room;

import java.time.Instant;

public record RoomInfoResponse(
    Integer roomId,
    String roomName,
    String roomDescription,
    Integer creatorId,
    Instant createdAt,
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
