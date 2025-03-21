package com.codemeet.service;

import com.codemeet.entity.User;
import com.codemeet.utils.dto.UserInfoResponse;
import com.codemeet.utils.dto.UserLoginRequest;
import com.codemeet.utils.dto.UserSignupRequest;
import com.codemeet.utils.exception.AuthenticationException;
import com.codemeet.utils.exception.DuplicateResourceException;
import com.codemeet.utils.exception.ResourceType;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    
    private final UserService userService;
    
    public AuthenticationService(UserService userService) {
        this.userService = userService;
    }
    
    public UserInfoResponse login(UserLoginRequest loginRequest) {
        User user = userService.getUserEntityByUsername(loginRequest.username());
        
        if (user.getPassword().equals(loginRequest.password())) {
            return UserInfoResponse.of(user);
        }
        
        throw new AuthenticationException("Unauthenticated login attempt");
    }
    
    public UserInfoResponse signup(UserSignupRequest signupRequest) {
        if (userService.existsByUsername(signupRequest.username())) {
            throw new DuplicateResourceException(
                "Username '%s' already in use".formatted(signupRequest.username()),
                ResourceType.USERNAME
            );
        }
        
        User user = new User();
        user.setFirstName(signupRequest.firstName());
        user.setLastName(signupRequest.lastName());
        user.setUsername(signupRequest.username());
        user.setEmail(signupRequest.email());
        user.setPassword(signupRequest.password());
        user.setPhoneNumber(signupRequest.phoneNumber());
        
        return UserInfoResponse.of(userService.save(user));
    }
}
