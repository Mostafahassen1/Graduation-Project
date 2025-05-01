package com.codemeet.service;

import com.codemeet.entity.*;
import com.codemeet.repository.MembershipRepository;
import com.codemeet.utils.dto.membership.MembershipInfoResponse;
import com.codemeet.utils.dto.membership.MembershipRequest;
import com.codemeet.utils.dto.notification.NotificationInfo;
import com.codemeet.utils.dto.room.RoomInfoResponse;
import com.codemeet.utils.dto.user.UserInfoResponse;
import com.codemeet.utils.exception.DuplicateResourceException;
import com.codemeet.utils.exception.EntityNotFoundException;
import com.codemeet.utils.exception.IllegalActionException;
import com.codemeet.utils.exception.ResourceType;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.codemeet.entity.NotificationType.MEMBERSHIP_ACCEPTED;
import static com.codemeet.entity.NotificationType.MEMBERSHIP_REQUEST;
@Service
public class MembershipService {
    
    private final MembershipRepository membershipRepository;
    private final NotificationService notificationService;
    private final UserService userService;
    private final RoomService roomService;
    private final ChatService chatService;
    
    public MembershipService(
        MembershipRepository membershipRepository,
        NotificationService notificationService,
        UserService userService,
        @Lazy RoomService roomService,
        ChatService chatService
    ) {
        this.membershipRepository = membershipRepository;
        this.notificationService = notificationService;
        this.userService = userService;
        this.roomService = roomService;
        this.chatService = chatService;
    }
    
    public boolean exists(Integer userId, Integer roomId) {
        return membershipRepository.exists(userId, roomId);
    }
    
    public Membership getMembershipEntity(int membershipId) {
        return membershipRepository.findById(membershipId)
            .orElseThrow(() -> new EntityNotFoundException(
                "Membership with id '%d' not found"
                    .formatted(membershipId)));
    }
    
    public Membership getMembershipEntity(int userId, int roomId) {
        userService.getUserEntityById(userId);
        roomService.getRoomEntityById(roomId);
        return membershipRepository.findByUserIdAndRoomId(userId, roomId)
            .orElseThrow(() -> new EntityNotFoundException(
                "User with id '%d' is not a member of room with id '%d'"
                    .formatted(userId, roomId)));
    }
    
    public List<Room> getAllRoomEntitiesOfUser(int userId) {
        userService.getUserEntityById(userId);
        return membershipRepository.findAllOfUser(userId).stream()
            .map(Membership::getRoom)
            .toList();
    }
    
    public List<User> getAllUserEntitiesOfRoom(int roomId) {
        roomService.getRoomEntityById(roomId);
        return membershipRepository.findAllOfRoom(roomId).stream()
            .map(Membership::getUser)
            .toList();
    }
    
    public Membership addMembershipEntity(Membership membership) {
        return membershipRepository.save(membership);
    }
    
    public List<RoomInfoResponse> getAllRoomsOfUser(int userId) {
        List<RoomInfoResponse> rooms = getAllRoomEntitiesOfUser(userId).stream()
            .map(RoomInfoResponse::of)
            .toList();
        System.out.println(rooms);
        return rooms;
    }
    
    public List<UserInfoResponse> getAllUsersOfRoom(int roomId) {
        return getAllUserEntitiesOfRoom(roomId).stream()
            .map(UserInfoResponse::of)
            .toList();
    }
    
    @Transactional
    public MembershipInfoResponse requestMembership(MembershipRequest joinRequest) {
        User user = userService.getUserEntityById(joinRequest.userId());
        Room room = roomService.getRoomEntityById(joinRequest.roomId());

        if (this.exists(user.getId(), room.getId())) {
            throw new DuplicateResourceException(
                "User with id '%d' already exists in room with id '%d'"
                    .formatted(user.getId(), room.getId()), ResourceType.MEMBERSHIP);
        }

        Membership membership = Membership.builder()
                        .user(user)
                                .room(room)
                                        .status(MembershipStatus.PENDING)
                                                .build();

        this.addMembershipEntity(membership);
        
        // Send notification to the admin...
        TransactionSynchronizationManager.registerSynchronization(
            new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    Map<String, Object> info = new LinkedHashMap<>();
                    info.put("roomId", membership.getRoom().getId());
                    info.put("roomName", membership.getRoom().getName());
                    info.put("requesterId", user.getId());
                    info.put("requesterName", user.getFullName());
                    info.put("requesterUsername", user.getUsername());
                    
                    // When the client clicks on the notification, it should
                    // be forwarded to room membership requests.
                    notificationService.sendToUser(new NotificationInfo(
                        info, room.getCreator().getId(), MEMBERSHIP_REQUEST
                    ));
                }
            }
        );

        return MembershipInfoResponse.of(membership);
    }
    
    @Transactional
    public void acceptMembership(int membershipId) {
        Membership membership = getMembershipEntity(membershipId);
        membership.setStatus(MembershipStatus.ACCEPTED);
        
        RoomChat rc = RoomChat.builder()
                .owner(membership.getUser())
                        .room(membership.getRoom())
                                .build();
        chatService.save(rc);
        
        // Send notification to the requester...
        TransactionSynchronizationManager.registerSynchronization(
            new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    Map<String, Object> info = new LinkedHashMap<>();
                    info.put("roomId", membership.getRoom().getId());
                    info.put("roomName", membership.getRoom().getName());
                    
                    // When the client clicks on the notification, it should
                    // be forwarded to room view.
                    notificationService.sendToUser(new NotificationInfo(
                        info, membership.getUser().getId(), MEMBERSHIP_ACCEPTED
                    ));
                }
            }
        );
    }
    
    @Transactional
    public void cancelMembership(int membershipId) {
        Membership membership = getMembershipEntity(membershipId);

        if (membership.getStatus().equals(MembershipStatus.ADMIN)) {
            throw new IllegalActionException(
                "Admin can't be removed from the room");
        }

        membershipRepository.deleteById(membershipId);
    }
}
