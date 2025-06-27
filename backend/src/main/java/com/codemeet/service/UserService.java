package com.codemeet.service;

import java.util.List;

import com.codemeet.utils.dto.user.UserInfoResponse;
import com.codemeet.utils.dto.user.UserUpdateRequest;
import com.codemeet.utils.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import com.codemeet.entity.User;
import com.codemeet.repository.UserRepository;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    
    public boolean existsById(int userId) {
        return userRepository.existsById(userId);
    }
    
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
    
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    public User save(User user) {
        return userRepository.save(user);
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
    
    public List<User> getAllUserEntities() {
        return userRepository.findAll();
    }
    
    public User updateUserEntity(User user) {
        if (userRepository.existsById(user.getId())) {
            return userRepository.save(user);
        } else {
            throw new EntityNotFoundException(
                "User with id '%d' not found".formatted(user.getId()));
        }
    }
    
    public List<User> searchForUserEntitiesByUsername(String query) {
        return userRepository.findByUsernameContaining(query);
    }
    
    public List<User> searchForUserEntitiesByUsernameAndFullName(String query) {
        return userRepository.findByUsernameContainingOrFullNameContainingIgnoreCase(query);
    }
    
    public UserInfoResponse getUserById(int id) {
        return UserInfoResponse.of(getUserEntityById(id));
    }
    
    public UserInfoResponse getUserByUsername(String username) {
        return UserInfoResponse.of(getUserEntityByUsername(username));
    }
    
    public List<UserInfoResponse> getAllUsers() {
        return getAllUserEntities().stream()
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
    
    public List<UserInfoResponse> searchForUsersByUsername(String query) {
        return searchForUserEntitiesByUsername(query).stream()
            .map(UserInfoResponse::of)
            .toList();
    }
    
    public List<UserInfoResponse> searchForUsersByUsernameAndFullName(String query) {
        return searchForUserEntitiesByUsernameAndFullName(query).stream()
            .map(UserInfoResponse::of)
            .toList();
    }
}
