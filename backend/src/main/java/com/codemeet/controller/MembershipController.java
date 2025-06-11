package com.codemeet.controller;

import com.codemeet.service.MembershipService;
import com.codemeet.utils.dto.membership.MembershipInfoResponse;
import com.codemeet.utils.dto.membership.MembershipRequest;
import com.codemeet.utils.dto.room.RoomInfoResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/membership")
public class MembershipController {
    
    private final MembershipService membershipService;

    @GetMapping("/room/{roomId}/memberships")
    public ResponseEntity<List<MembershipInfoResponse>> getAllMembershipsByRoomId(
        @PathVariable int roomId
    ) {
        return ResponseEntity.ok(membershipService.getAllMembershipsByRoomId(roomId));
    }
    
    @GetMapping("/room/{roomId}/memberships/accepted")
    public ResponseEntity<List<MembershipInfoResponse>> getAllAcceptedMembershipsByRoomId(
        @PathVariable int roomId
    ) {
        return ResponseEntity.ok(membershipService.getAllAcceptedMembershipsByRoomId(roomId));
    }
    
    @GetMapping("/room/{roomId}/memberships/pending")
    public ResponseEntity<List<MembershipInfoResponse>> getAllPendingMembershipsByRoomId(
        @PathVariable int roomId
    ) {
        return ResponseEntity.ok(membershipService.getAllPendingMembershipsByRoomId(roomId));
    }
    
    @GetMapping("/user/{userId}/rooms")
    public ResponseEntity<List<RoomInfoResponse>> getAllRoomsByUserId(
        @PathVariable int userId
    ) {
        return ResponseEntity.ok(membershipService.getAllRoomsByUserId(userId));
    }
    
    @PostMapping("/request")
    public ResponseEntity<MembershipInfoResponse> requestMembership(
        @RequestBody MembershipRequest joinRequest
    ) {
        return ResponseEntity.ok(membershipService.requestMembership(joinRequest));
    }
    
    @PatchMapping("/accept")
    public ResponseEntity<Void> acceptMembership(
        @RequestBody MembershipRequest joinRequest
    ) {
        membershipService.acceptMembership(joinRequest);
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/cancel")
    public ResponseEntity<Void> cancelMembership(
        @RequestBody MembershipRequest cancelRequest
    ) {
        membershipService.cancelMembership(cancelRequest);
        return ResponseEntity.noContent().build();
    }
}
