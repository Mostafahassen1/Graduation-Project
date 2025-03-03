package com.codemeet.service;

import com.codemeet.entity.Room;
import com.codemeet.entity.User;
import com.codemeet.repository.RoomRepository;
import com.codemeet.utils.dto.MembershipRequest;
import com.codemeet.utils.dto.RoomCreationRequest;
import com.codemeet.utils.dto.RoomInfoResponse;
import com.codemeet.utils.dto.RoomUpdateRequest;
import com.codemeet.utils.exception.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final UserService userService;
    private final MembershipService membershipService;
    
    public RoomService(
        RoomRepository roomRepository,
        UserService userService,
        MembershipService membershipService
    ) {
        this.roomRepository = roomRepository;
        this.userService = userService;
        this.membershipService = membershipService;
    }
    
    public boolean exists(int roomId) {
        return roomRepository.existsById(roomId);
    }
    
    public Room getRoomEntityById(int id) {
        return roomRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(
                "Room with id '%d' not found".formatted(id)));
    }
    
    public List<Room> getAllRoomEntities(Integer userId) {
        return roomRepository.getAllByCreatorId(userId );
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
        Room room = new Room(
            creationRequest.name(),
            creationRequest.description(),
            creator,
            creationRequest.roomPictureUrl()
        );

        addRoomEntity(room);

        membershipService.requestMembership(
            new MembershipRequest(room.getCreator().getId(), room.getId()));

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
