package com.codemeet.controller;

import com.codemeet.service.FriendshipService;
import com.codemeet.utils.dto.friendship.FriendshipInfoResponse;
import com.codemeet.utils.dto.friendship.FriendshipRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@AllArgsConstructor
@RestController
@RequestMapping("/api/friendship")
public class FriendshipController {

    private final FriendshipService friendshipService;


    @GetMapping("/{userId}")
    public ResponseEntity<List<FriendshipInfoResponse>> getAllFriendships(
        @PathVariable Integer userId
    ) {
        return ResponseEntity.ok(friendshipService.getAllFriendships(userId));
    }
    
    @GetMapping("/accepted/{userId}")
    public ResponseEntity<List<FriendshipInfoResponse>> getAllAcceptedFriendships(
        @PathVariable Integer userId
    ) {
        return ResponseEntity.ok(friendshipService.getAllAcceptedFriendships(userId));
    }
    
    @GetMapping("/pending/{userId}/sent")
    public ResponseEntity<List<FriendshipInfoResponse>> getAllPendingSentFriendships(
        @PathVariable Integer userId
    ) {
        return ResponseEntity.ok(friendshipService.getAllPendingSentFriendships(userId));
    }
    
    @GetMapping("/pending/{userId}/received")
    public ResponseEntity<List<FriendshipInfoResponse>> getAllPendingReceivedFriendships(
        @PathVariable Integer userId
    ) {
        return ResponseEntity.ok(friendshipService.getAllPendingReceivedFriendships(userId));
    }

    @PostMapping("/request")
    public ResponseEntity<Integer> requestFriendship(
        @RequestBody FriendshipRequest friendshipRequest
    ) {
        return ResponseEntity.ok(friendshipService.requestFriendship(friendshipRequest));
    }
    
    @DeleteMapping("/cancel/{friendshipId}")
    public ResponseEntity<Void> cancelFriendship(
        @PathVariable Integer friendshipId
    ) {
        friendshipService.cancelFriendship(friendshipId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/accept/{friendshipId}")
    public ResponseEntity<Void> acceptFriendship(
        @PathVariable Integer friendshipId
    ) {
        friendshipService.acceptFriendship(friendshipId);
        return ResponseEntity.noContent().build();
    }
}
