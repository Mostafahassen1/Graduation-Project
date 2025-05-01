package com.codemeet.service;

import com.codemeet.entity.*;
import com.codemeet.repository.RoomRepository;
import com.codemeet.utils.dto.RoomCreationRequest;
import com.codemeet.utils.dto.RoomInfoResponse;
import com.codemeet.utils.dto.RoomUpdateRequest;
import com.codemeet.utils.exception.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@AllArgsConstructor
@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final UserService userService;
    private final MembershipService membershipService;
    private final ChatService chatService;
    


    
    public Room getRoomEntityById(int id) {
        return roomRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(
                "Room with id '%d' not found".formatted(id)));
    }
    
    public List<Room> getAllRoomEntities(Integer userId) {
        return roomRepository.findAllByCreatorId(userId );
    }
    
    public Room addRoomEntity(Room room) {
        return roomRepository.save(room);
    }
    
    public Room updateRoomEntity(Room room) {
        if (roomRepository.existsById(room.getId())) {
            return roomRepository.save(room);
        } else {
            throw new EntityNotFoundException(
                "Room with id '%d' not found".formatted(room.getId()));
        }
    }
    
    public RoomInfoResponse getRoomById(int id) {
        Room room = getRoomEntityById(id);
        return RoomInfoResponse.of(room);
    }
    
    public List<RoomInfoResponse> getAllRoomsByCreator(Integer userId) {
        return getAllRoomEntities(userId).stream()
            .map(RoomInfoResponse::of)
            .toList();
    }

    @Transactional
    public RoomInfoResponse createRoom(RoomCreationRequest creationRequest) {
        User creator = userService.getUserEntityById(creationRequest.creatorId());

        Room room = Room.builder()
                .name(creationRequest.name())
                .description(creationRequest.description())
                .creator(creator)
                .roomPictureUrl(creationRequest.roomPictureUrl())
                .build();
        this.addRoomEntity(room);

        Membership membership = Membership.builder()
                .user(creator)
                .room(room)
                .status(MembershipStatus.ADMIN)
                .build();
        membershipService.addMembershipEntity(membership);

        RoomChat chat = RoomChat.builder()
                .owner(creator)
                .room(room)
                .build();
        chatService.save(chat);

        return RoomInfoResponse.of(room);
    }

    @Transactional
    public RoomInfoResponse updateRoom(RoomUpdateRequest updateRequest) {
        Room room = getRoomEntityById(updateRequest.roomId()); // Persisted
        room.setName(updateRequest.name());
        room.setDescription(updateRequest.description());
        room.setRoomPictureUrl(updateRequest.roomPictureUrl());
        return RoomInfoResponse.of(room);
    }
}
