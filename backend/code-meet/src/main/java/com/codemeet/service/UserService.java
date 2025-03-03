package com.codemeet.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.codemeet.utils.dto.UserInfoResponse;
import com.codemeet.utils.dto.UserUpdateRequest;
import com.codemeet.utils.exception.EntityNotFoundException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.codemeet.entity.User;
import com.codemeet.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public boolean exists(int userId) {
        return userRepository.existsById(userId);
    }
    
    public User getUserEntityById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                    "User with id '%d' not found".formatted(id)));
    }

    public User getUserEntityByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(
                    "User with username '%s' not found".formatted(username)));
    }
    
    @Async
    public CompletableFuture<List<User>> getAllUserEntities() {
        return CompletableFuture.completedFuture(userRepository.findAll());
    }
    
    public User updateUserEntity(User user) {
        if (userRepository.existsById(user.getId())) {
            return userRepository.save(user);
        } else {
            throw new EntityNotFoundException(
                "User with id '%d' not found".formatted(user.getId()));
        }
    }
    
    public UserInfoResponse getUserById(int id) {
        return UserInfoResponse.of(getUserEntityById(id));
    }
    
    public UserInfoResponse getUserByUsername(String username) {
        return UserInfoResponse.of(getUserEntityByUsername(username));
    }
    
    public List<UserInfoResponse> getAllUsers() {
        return getAllUserEntities().join().stream()
            .map(UserInfoResponse::of)
            .toList();
    }
    
    public UserInfoResponse updateUser(UserUpdateRequest updateRequest) {
        User user = getUserEntityById(updateRequest.userId()); // Persisted
        user.setFirstName(updateRequest.firstName());
        user.setLastName(updateRequest.lastName());
        user.setUsername(updateRequest.username());
        user.setEmail(updateRequest.email());
        user.setPassword(updateRequest.password());
        user.setPhoneNumber(updateRequest.phoneNumber());
        user.setProfilePictureUrl(updateRequest.profilePictureUrl());
        return UserInfoResponse.of(user);
    }
}
