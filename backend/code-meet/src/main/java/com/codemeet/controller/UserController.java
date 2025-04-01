package com.codemeet.controller;

import java.util.List;

import com.codemeet.entity.User;
import com.codemeet.utils.dto.UserUpdateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.codemeet.service.UserService;
import com.codemeet.utils.dto.UserInfoResponse;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserInfoResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserInfoResponse> getUserById(@PathVariable int id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
    
    @GetMapping("/current")
    public ResponseEntity<User> getCurrentUser() {
        return ResponseEntity.ok(userService.getCurrentUser() );
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
}
