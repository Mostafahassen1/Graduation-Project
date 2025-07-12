package com.codemeet.service;

import java.util.List;

import com.codemeet.utils.FileUploadUtil;
import com.codemeet.utils.dto.cloudinary.CloudinaryInfoResponse;
import com.codemeet.utils.dto.user.UserInfoResponse;
import com.codemeet.utils.dto.user.UserUpdateRequest;
import com.codemeet.utils.exception.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import com.codemeet.entity.User;
import com.codemeet.repository.UserRepository;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final CloudinaryService cloudinary;

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

    @Transactional
    public UserInfoResponse updateUser(UserUpdateRequest updateRequest) {
        User user = getUserEntityById(updateRequest.userId()); // Persisted
        user.setFirstName(updateRequest.firstName());
        user.setLastName(updateRequest.lastName());
        user.setUsername(updateRequest.username());
        user.setGender(updateRequest.gender());
        user.setBio(updateRequest.bio());
        return UserInfoResponse.of(user);
    }

    @Transactional
    public UserInfoResponse updateProfilePicture(Integer userId, MultipartFile image) {
        User user = getUserEntityById(userId);

        FileUploadUtil.assertAllowed(image, FileUploadUtil.IMAGE_PATTERN);
        CloudinaryInfoResponse response = cloudinary.updateProfilePicture(image, userId);
        user.setProfilePictureUrl(response.url());

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
