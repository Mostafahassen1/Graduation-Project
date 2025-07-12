package com.codemeet.controller;

import java.io.IOException;
import java.util.List;

import com.codemeet.utils.dto.user.UserUpdateRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.codemeet.service.UserService;
import com.codemeet.utils.dto.user.UserInfoResponse;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/all")
    public ResponseEntity<List<UserInfoResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<UserInfoResponse>> searchForUsers(
        @RequestParam String query,
        @RequestParam boolean uno
    ) {
        return ResponseEntity.ok(
            uno ? userService.searchForUsersByUsername(query) :
                userService.searchForUsersByUsernameAndFullName(query)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserInfoResponse> getUserById(@PathVariable int id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping
    public ResponseEntity<UserInfoResponse> getUsersByName(
        @RequestParam String username
    ) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    @PutMapping("/update")
    public ResponseEntity<UserInfoResponse> update(
        @RequestBody UserUpdateRequest updateRequest
    ) {
        return ResponseEntity.ok(userService.updateUser(updateRequest));
    }

    @PostMapping("/update/{userId}/profilePicture")
    public ResponseEntity<UserInfoResponse> updateProfilePicture(
        @PathVariable Integer userId,
        @RequestPart MultipartFile image
    ) {
        return ResponseEntity.ok(userService.updateProfilePicture(userId, image));
    }
}
