package com.codemeet.service;

import com.codemeet.entity.Friendship;
import com.codemeet.entity.FriendshipStatus;
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

import java.util.List;
import java.util.Optional;

@Service
public class FriendshipService {

    private final FriendshipRepository friendshipRepository;
    private final UserService userService;

    public FriendshipService(
        FriendshipRepository friendshipRepository,
        UserService userService
    ) {
        this.friendshipRepository = friendshipRepository;
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

        Optional<Friendship> f = friendshipRepository.findByFromIdAndToId(
            friendshipRequest.fromId(), friendshipRequest.toId()
        );

        if (f.isPresent()) {
            throw new DuplicateResourceException(
                "Friendship between user with id '%d' and user with id '%d' already exists (%s)"
                    .formatted(
                        f.get().getFrom().getId(),
                        f.get().getTo().getId(),
                        f.get().getStatus()
                    ),
                ResourceType.FRIENDSHIP
            );
        }

        User from = userService.getUserEntityById(friendshipRequest.fromId());
        User to = userService.getUserEntityById(friendshipRequest.toId());

        Friendship friendship = new Friendship(from, to, FriendshipStatus.PENDING);
        friendshipRepository.save(friendship);

        //TODO: send notification with FriendshipResponse to requested user to that this user wants to be his friend
        return friendship.getId();
    }

    @Transactional
    public void cancelFriendship(Integer friendshipId) {
        Friendship friendship = getFriendshipEntityById(friendshipId);
        friendshipRepository.delete(friendship);
    }

    @Transactional
    public void acceptFriendshipRequest(Integer friendshipId) {
        Friendship friendship = getFriendshipEntityById(friendshipId);

        if (friendship.getStatus().equals(FriendshipStatus.PENDING)) {
            friendship.setStatus(FriendshipStatus.ACCEPTED);
        } else {
            throw new IllegalActionException(
                "Friendship state should be pending in order to accept it");
        }
    }
}
