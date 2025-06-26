package com.codemeet.service;

import com.codemeet.entity.*;
import com.codemeet.repository.FriendshipRepository;
import com.codemeet.utils.dto.friendship.FriendshipInfoResponse;
import com.codemeet.utils.dto.friendship.FriendshipRequest;
import com.codemeet.utils.dto.notification.NotificationInfoResponse;
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
    
    public FriendshipInfoResponse getFriendshipById(Integer friendshipId) {
        return FriendshipInfoResponse.of(getFriendshipEntityById(friendshipId), null);
    }
    
    public FriendshipInfoResponse getFriendshipByFromIdAndToId(Integer fromId, Integer toId) {
        return FriendshipInfoResponse.of(
            getFriendshipEntityByFromIdAndToId(fromId, toId), fromId);
    }

    public List<FriendshipInfoResponse> getAllFriendships(Integer userId) {
        return getAllFriendshipEntities(userId).stream()
            .map(f -> FriendshipInfoResponse.of(f, userId))
            .toList();
    }

    public List<FriendshipInfoResponse> getAllAcceptedFriendships(Integer userId) {
        return friendshipRepository.findAllAcceptedByUserId(userId).stream()
            .map(f -> FriendshipInfoResponse.of(f, userId))
            .toList();
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
    public FriendshipInfoResponse requestFriendship(FriendshipRequest friendshipRequest) {
        if (friendshipRequest.fromId().equals(friendshipRequest.toId())) {
            throw new IllegalActionException(
                "User can't send friendship request to himself");
        }

        Optional<Friendship> ofs = friendshipRepository.findByFromIdAndToId(
            friendshipRequest.fromId(), friendshipRequest.toId()
        );

        if (ofs.isPresent()) {
            throw new DuplicateResourceException(
                "Friendship between user with id '%d' and user with id '%d' already exists (%s)"
                    .formatted(
                        ofs.get().getFrom().getId(),
                        ofs.get().getTo().getId(),
                        ofs.get().getStatus()
                    ),
                ResourceType.FRIENDSHIP
            );
        }

        User from = userService.getUserEntityById(friendshipRequest.fromId());
        User to = userService.getUserEntityById(friendshipRequest.toId());
        Friendship fs = Friendship.builder()
           .from(from)
           .to(to)
           .status(PENDING)
           .build();
        friendshipRepository.save(fs);

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
                    notificationService.sendToUser(new NotificationInfoResponse(
                        info, to.getId(), FRIENDSHIP_REQUEST
                    ));
                }
            }
        );
        
        return FriendshipInfoResponse.of(fs, friendshipRequest.fromId());
    }

    @Transactional
    public void cancelFriendship(Integer friendshipId) {
        Friendship friendship = getFriendshipEntityById(friendshipId);
        //TODO: We may need to handle something in chats...
        friendshipRepository.delete(friendship);
    }

    @Transactional
    public FriendshipInfoResponse acceptFriendship(FriendshipRequest friendshipRequest) {
        Friendship fs = getFriendshipEntityByFromIdAndToId(
            friendshipRequest.fromId(),
            friendshipRequest.toId()
        );

        if (fs.getStatus() == PENDING) {
            fs.setStatus(ACCEPTED);
            
            // Send notification...
            TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        Map<String, Object> info = new LinkedHashMap<>();
                        info.put("acceptorUsername", fs.getTo().getUsername());
                        info.put("acceptorFullName", fs.getTo().getFullName());
                        
                        // When the client clicks on the notification, it should
                        // be forward to the friend profile.
                        notificationService.sendToUser(new NotificationInfoResponse(
                            info, fs.getFrom().getId(), FRIENDSHIP_ACCEPTED
                        ));
                    }
                }
            );
            
            // Create a chat between them...
            if (!chatService.peerChatExistsByOwnerIdAndPeerId(
                fs.getFrom().getId(), fs.getTo().getId())) {
                chatService.save(PeerChat.builder()
                    .peer(fs.getTo())
                    .owner(fs.getFrom())
                    .build());
            }
            
            if (!chatService.peerChatExistsByOwnerIdAndPeerId(
                fs.getTo().getId(), fs.getFrom().getId())) {
                chatService.save(PeerChat.builder()
                    .owner(fs.getTo())
                    .peer(fs.getFrom())
                    .build());
            }
            
            return FriendshipInfoResponse.of(fs, friendshipRequest.fromId());
        } else {
            throw new IllegalActionException(
                "Friendship status should be PENDING in order to be accepted");
        }
    }
}
