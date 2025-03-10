package com.codemeet.service;

import com.codemeet.entity.Friendship;
import com.codemeet.entity.FriendshipStatus;
import com.codemeet.entity.Notification;
import com.codemeet.entity.User;
import com.codemeet.repository.FriendshipRepository;
import com.codemeet.utils.dto.FriendshipRequest;
import com.codemeet.utils.dto.FriendshipResponse;
import com.codemeet.utils.exception.DuplicateResourceException;
import com.codemeet.utils.exception.EntityNotFoundException;
import com.codemeet.utils.exception.IllegalActionException;
import com.codemeet.utils.exception.ResourceType;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;
import java.util.Optional;

import static com.codemeet.entity.NotificationType.*;

@Service
public class FriendshipService {
    
    private final FriendshipRepository friendshipRepository;
    private final NotificationService notificationService;
    private final UserService userService;

    public FriendshipService(
        FriendshipRepository friendshipRepository,
        NotificationService notificationService,
        UserService userService
    ) {
        this.friendshipRepository = friendshipRepository;
        this.notificationService = notificationService;
        this.userService = userService;
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

    public List<FriendshipResponse> getAllFriendships(Integer userId) {
        return getAllFriendshipEntities(userId).stream()
            .map(f -> FriendshipResponse.of(f, userId))
            .toList();
    }

    public List<FriendshipResponse> getAllAcceptedFriendships(Integer userId) {
        return friendshipRepository.getAllFriends(userId);
    }

    public List<FriendshipResponse> getAllPendingSentFriendships(Integer userId) {
        return getAllPendingSentFriendshipEntities(userId).stream()
            .map(f -> FriendshipResponse.of(f, userId))
            .toList();
    }

    public List<FriendshipResponse> getAllPendingReceivedFriendships(Integer userId) {
        return getAllPendingReceivedFriendshipEntities(userId).stream()
            .map(f -> FriendshipResponse.of(f, userId))
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

        Friendship f = new Friendship(from, to, FriendshipStatus.PENDING);
        friendshipRepository.save(f);

        // Sending notification...
        TransactionSynchronizationManager.registerSynchronization(
            new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    Notification n = new Notification();
                    n.setReceiver(to);
                    n.setContent(FRIENDSHIP_REQUEST.getAbstractContent()
                        .formatted(from.getFullName(), from.getUsername()));
                    notificationService.sendToUser(to.getId(), n);
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
        Friendship f = getFriendshipEntityById(friendshipId);

        if (f.getStatus() == FriendshipStatus.PENDING) {
            f.setStatus(FriendshipStatus.ACCEPTED);
            
            // Sending notification...
            TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        Notification n = new Notification();
                        n.setReceiver(f.getFrom());
                        n.setContent(FRIENDSHIP_ACCEPTED.getAbstractContent()
                            .formatted(f.getTo().getFullName(), f.getTo().getUsername()));
                        notificationService.sendToUser(f.getFrom().getId(), n);
                    }
                }
            );
            
            //TODO: create chat between them...
        } else {
            throw new IllegalActionException(
                "Friendship status should be PENDING in order to be accepted");
        }
    }
}
