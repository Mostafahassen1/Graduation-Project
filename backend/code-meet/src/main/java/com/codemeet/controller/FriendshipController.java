package com.codemeet.controller;

import com.codemeet.service.FriendshipService;
import com.codemeet.utils.dto.FriendshipRequest;
import com.codemeet.utils.dto.FriendshipResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friendship")
public class FriendshipController {

    private final FriendshipService friendshipService;

    public FriendshipController(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<FriendshipResponse>> getAllFriendships(
        @PathVariable Integer userId
    ) {
        return ResponseEntity.ok(friendshipService.getAllFriendships(userId));
    }

    @PostMapping("/request")
    public ResponseEntity<Integer> askFriendshipRequest(
        @RequestBody FriendshipRequest friendshipRequest
    ) {
        return ResponseEntity.ok(friendshipService.askFriendshipRequest(friendshipRequest));
    }

    //handles three cases
    @DeleteMapping("/cancel/{friendshipId}")
    public ResponseEntity<Void> cancelFriendship(
        @PathVariable Integer friendshipId
    ) {
        friendshipService.cancelFriendship(friendshipId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/accept/{friendshipId}")
    public ResponseEntity<Void> acceptFriendshipRequest(
        @PathVariable Integer friendshipId
    ) {
        friendshipService.acceptFriendshipRequest(friendshipId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/accepted/{userId}")
    public ResponseEntity<List<FriendshipResponse>> getAllAcceptedFriendships(
        @PathVariable Integer userId
    ) {
        return ResponseEntity.ok(friendshipService.getAllAcceptedFriendships(userId));
    }

    @GetMapping("/pending/{userId}/sent")
    public ResponseEntity<List<FriendshipResponse>> getAllPendingSentFriendships(
        @PathVariable Integer userId
    ) {
        return ResponseEntity.ok(friendshipService.getAllPendingSentFriendships(userId));
    }

    @GetMapping("/pending/{userId}/received")
    public ResponseEntity<List<FriendshipResponse>> getAllPendingReceivedFriendships(
        @PathVariable Integer userId
    ) {
        return ResponseEntity.ok(friendshipService.getAllPendingReceivedFriendships(userId));
    }
}
