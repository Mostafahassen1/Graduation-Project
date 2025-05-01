package com.codemeet.service;

import com.codemeet.entity.*;
import com.codemeet.repository.FriendshipRepository;
import com.codemeet.utils.dto.FriendshipInfoResponse;
import com.codemeet.utils.dto.FriendshipRequest;
import com.codemeet.utils.dto.NotificationInfo;
import com.codemeet.utils.exception.DuplicateResourceException;
import com.codemeet.utils.exception.EntityNotFoundException;
import com.codemeet.utils.exception.IllegalActionException;
import com.codemeet.utils.exception.ResourceType;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.codemeet.entity.FriendshipStatus.ACCEPTED;
import static com.codemeet.entity.FriendshipStatus.PENDING;
import static com.codemeet.entity.NotificationType.FRIENDSHIP_REQUEST;
import static com.codemeet.entity.NotificationType.FRIENDSHIP_ACCEPTED;

@Service
@AllArgsConstructor
public class FriendshipService {
    
    private final FriendshipRepository friendshipRepository;
    private final NotificationService notificationService;
    private final UserService userService;
    private final ChatService chatService;

    public boolean exists(Integer fromId, Integer toId) {
        return friendshipRepository.exists(fromId, toId);
    }

    public Friendship getFriendshipEntityById(Integer friendshipId) {
        return friendshipRepository.findById(friendshipId)
            .orElseThrow(() -> new EntityNotFoundException(
                "Friendship with id '%d' not found".formatted(friendshipId)));
    }

    public Friendship getFriendshipEntityByFromIdAndToId(Integer fromId, Integer toId) {
        return friendshipRepository.findByFromIdAndToId(fromId, toId)
            .orElseThrow(() -> new EntityNotFoundException(
                "Friendship between user with id '%d' and user with '%d' not found"
                    .formatted(fromId, toId)));
    }

    public List<Friendship> getAllFriendshipEntities(Integer userId) {
        userService.getUserEntityById(userId);
        return friendshipRepository.findAllByUserId(userId);
    }

    public List<Friendship> getAllAcceptedFriendshipEntities(Integer userId) {
        userService.getUserEntityById(userId);
        return friendshipRepository.findAllAcceptedByUserId(userId);
    }

    public List<Friendship> getAllPendingSentFriendshipEntities(Integer userId) {
        userService.getUserEntityById(userId);
        return friendshipRepository.findAllPendingSentByUserId(userId);
    }

    public List<Friendship> getAllPendingReceivedFriendshipEntities(Integer userId) {
        userService.getUserEntityById(userId);
        return friendshipRepository.findAllPendingReceivedByUserId(userId);
    }

    public List<FriendshipInfoResponse> getAllFriendships(Integer userId) {
        return getAllFriendshipEntities(userId).stream()
            .map(f -> FriendshipInfoResponse.of(f, userId))
            .toList();
    }

    public List<FriendshipInfoResponse> getAllAcceptedFriendships(Integer userId) {
        return friendshipRepository.getAllFriends(userId);
    }

    public List<FriendshipInfoResponse> getAllPendingSentFriendships(Integer userId) {
        return getAllPendingSentFriendshipEntities(userId).stream()
            .map(f -> FriendshipInfoResponse.of(f, userId))
            .toList();
    }

    public List<FriendshipInfoResponse> getAllPendingReceivedFriendships(Integer userId) {
        return getAllPendingReceivedFriendshipEntities(userId).stream()
            .map(f -> FriendshipInfoResponse.of(f, userId))
            .toList();
    }

    @Transactional
    public Integer askFriendshipRequest(FriendshipRequest friendshipRequest) {
        if (friendshipRequest.fromId().equals(friendshipRequest.toId())) {
            throw new IllegalActionException(
                "User can't send friendship request to himself");
        }

        Optional<Friendship> of = friendshipRepository.findByFromIdAndToId(
            friendshipRequest.fromId(), friendshipRequest.toId()
        );

        if (of.isPresent()) {
            throw new DuplicateResourceException(
                "Friendship between user with id '%d' and user with id '%d' already exists (%s)"
                    .formatted(
                        of.get().getFrom().getId(),
                        of.get().getTo().getId(),
                        of.get().getStatus()
                    ),
                ResourceType.FRIENDSHIP
            );
        }

        User from = userService.getUserEntityById(friendshipRequest.fromId());
        User to = userService.getUserEntityById(friendshipRequest.toId());
        Friendship f=  Friendship.builder()
                   .from(from)
                   .to(to)
                   .status(PENDING)
                   .build();
        friendshipRepository.save(f);

        // Sending notification...
        TransactionSynchronizationManager.registerSynchronization(
            new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    Map<String, Object> info = new LinkedHashMap<>();
                    info.put("senderUsername", from.getUsername());
                    info.put("senderFullName", from.getFullName());
                    
                    // When the client clicks on the notification, it should
                    // be forwarded to the friendship requests tab.
                    notificationService.sendToUser(new NotificationInfo(
                        info, to.getId(), FRIENDSHIP_REQUEST
                    ));
                }
            }
        );
        
        return f.getId();
    }

    @Transactional
    public void cancelFriendship(Integer friendshipId) {
        Friendship friendship = getFriendshipEntityById(friendshipId);
        friendshipRepository.delete(friendship);
    }

    @Transactional
    public void acceptFriendshipRequest(Integer friendshipId) {
        Friendship friendship = getFriendshipEntityById(friendshipId);

        if (friendship.getStatus() == PENDING) {
            friendship.setStatus(ACCEPTED);
            
            // Send notification...
            TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        Map<String, Object> info = new LinkedHashMap<>();
                        info.put("acceptorUsername", friendship.getTo().getUsername());
                        info.put("acceptorFullName", friendship.getTo().getFullName());
                        
                        // When the client clicks on the notification, it should
                        // be forward to the friend profile.
                        notificationService.sendToUser(new NotificationInfo(
                            info, friendship.getFrom().getId(), FRIENDSHIP_ACCEPTED
                        ));
                    }
                }
            );
            
            // Create a chat between them...
            PeerChat pc1 = PeerChat.builder()
                    .peer(friendship.getTo())
                    .owner(friendship.getFrom())
                    .build();
            PeerChat pc2 = PeerChat.builder()
                    .owner(friendship.getTo())
                    .peer(friendship.getFrom())
                    .build();

            
            chatService.saveAll(List.of(pc1, pc2));
        } else {
            throw new IllegalActionException(
                "Friendship status should be PENDING in order to be accepted");
        }
    }
}
