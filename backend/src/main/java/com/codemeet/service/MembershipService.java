package com.codemeet.service;

import com.codemeet.entity.*;
import com.codemeet.repository.MembershipRepository;
import com.codemeet.utils.dto.membership.MembershipInfoResponse;
import com.codemeet.utils.dto.membership.MembershipRequest;
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

import java.util.List;

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
    
    public Membership getMembershipEntityById(int membershipId) {
        return membershipRepository.findById(membershipId)
            .orElseThrow(() -> new EntityNotFoundException(
                "Membership with id '%d' not found"
                    .formatted(membershipId)));
    }
    
    public Membership getMembershipEntityByUserIdAndRoomId(int userId, int roomId) {
        userService.getUserEntityById(userId);
        roomService.getRoomEntityById(roomId);
        return membershipRepository.findByUserIdAndRoomId(userId, roomId)
            .orElseThrow(() -> new EntityNotFoundException(
                "User with id '%d' is not a member of room with id '%d'"
                    .formatted(userId, roomId)));
    }
    
    public List<Room> getAllRoomEntitiesByUserId(int userId) {
        userService.getUserEntityById(userId);
        return membershipRepository.findAllByUserId(userId).stream()
            .map(Membership::getRoom)
            .toList();
    }
    
    public List<User> getAllUserEntitiesByRoomId(int roomId) {
        roomService.getRoomEntityById(roomId);
        return membershipRepository.findAllByRoomId(roomId).stream()
            .map(Membership::getUser)
            .toList();
    }
    
    public List<Membership> getAllMembershipEntitiesByRoomId(int roomId) {
        roomService.getRoomEntityById(roomId);
        return membershipRepository.findAllByRoomId(roomId);
    }
    
    public Membership addMembershipEntity(Membership membership) {
        return membershipRepository.save(membership);
    }
    
    
    public List<RoomInfoResponse> getAllRoomsByUserId(int userId) {
        List<RoomInfoResponse> rooms = getAllRoomEntitiesByUserId(userId).stream()
            .map(RoomInfoResponse::of)
            .toList();
        return rooms;
    }
    
    public List<UserInfoResponse> getAllUsersByRoomId(int roomId) {
        return getAllUserEntitiesByRoomId(roomId).stream()
            .map(UserInfoResponse::of)
            .toList();
    }
    
    public List<MembershipInfoResponse> getAllMembershipsByRoomId(int roomId) {
        return getAllMembershipEntitiesByRoomId(roomId).stream()
            .map(MembershipInfoResponse::of)
            .toList();
    }
    
    public List<MembershipInfoResponse> getAllAcceptedMembershipsByRoomId(int roomId) {
        return getAllMembershipEntitiesByRoomId(roomId).stream()
            .filter(ms -> ms.getStatus() != MembershipStatus.PENDING)
            .map(MembershipInfoResponse::of)
            .toList();
    }
    
    public List<MembershipInfoResponse> getAllPendingMembershipsByRoomId(int roomId) {
        return getAllMembershipEntitiesByRoomId(roomId).stream()
            .filter(ms -> ms.getStatus() == MembershipStatus.PENDING)
            .map(MembershipInfoResponse::of)
            .toList();
    }
    
    @Transactional
    public MembershipInfoResponse requestMembership(MembershipRequest joinRequest) {
        User sender = userService.getUserEntityById(joinRequest.userId());
        Room room = roomService.getRoomEntityById(joinRequest.roomId());

        if (this.exists(sender.getId(), room.getId())) {
            throw new DuplicateResourceException(
                "User with id '%d' already exists in room with id '%d'"
                    .formatted(sender.getId(), room.getId()), ResourceType.MEMBERSHIP);
        }

        Membership membership = Membership.builder()
            .user(sender)
            .room(room)
            .status(MembershipStatus.PENDING)
            .build();

        this.addMembershipEntity(membership);
        
        // Send notification to the admin...
        TransactionSynchronizationManager.registerSynchronization(
            new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    // When the client clicks on the notification, it should
                    // be forwarded to room membership requests.
                    notificationService.sendMembershipRequestNotification(sender, room, room.getCreator());
                }
            }
        );

        return MembershipInfoResponse.of(membership);
    }
    
    @Transactional
    public void acceptMembership(MembershipRequest joinRequest) {
        accept(getMembershipEntityByUserIdAndRoomId(
            joinRequest.userId(),
            joinRequest.roomId()
        ));
    }
    
    @Transactional
    public void acceptMembership(Integer membershipId) {
        accept(getMembershipEntityById(membershipId));
    }
    
    @Transactional
    protected void accept(Membership membership) {
        membership.setStatus(MembershipStatus.ACCEPTED);
        
        if (!chatService.roomChatExistsByOwnerIdAndRoomId(
            membership.getUser().getId(), membership.getRoom().getId())
        ) {
            RoomChat rc = RoomChat.builder()
                .owner(membership.getUser())
                .room(membership.getRoom())
                .build();
            
            chatService.save(rc);
        }
        
        // Send notification to the requester...
        TransactionSynchronizationManager.registerSynchronization(
            new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    // When the client clicks on the notification, it should
                    // be forwarded to room view.
                    notificationService.sendMembershipAcceptedNotification(membership.getRoom(), membership.getUser());
                }
            }
        );
    }
    
    @Transactional
    public void cancelMembership(MembershipRequest cancelRequest) {
        cancel(getMembershipEntityByUserIdAndRoomId(
            cancelRequest.userId(),
            cancelRequest.roomId()
        ));
    }
    
    @Transactional
    public void cancelMembership(Integer membershipId) {
        cancel(getMembershipEntityById(membershipId));
    }
    
    @Transactional
    protected void cancel(Membership membership) {
        if (membership.getStatus() == MembershipStatus.ADMIN) {
            //TODO: A more practical approach must be implemented...
            throw new IllegalActionException(
                "Admin can't be removed from the room");
        }
        
        membershipRepository.deleteById(membership.getId());
    }
}
