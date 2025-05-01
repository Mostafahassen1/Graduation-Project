package com.codemeet.service;

import com.codemeet.entity.User;
import com.codemeet.utils.dto.user.UserInfoResponse;
import com.codemeet.utils.dto.user.UserLoginRequest;
import com.codemeet.utils.dto.user.UserSignupRequest;
import com.codemeet.utils.exception.AuthenticationException;
import com.codemeet.utils.exception.DuplicateResourceException;
import com.codemeet.utils.exception.ResourceType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationService {
    
    private final UserService userService;

    
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
        
        User user = User.builder()
                .firstName(signupRequest.firstName())
                .lastName(signupRequest.lastName())
                .email(signupRequest.email())
                .username(signupRequest.username())
                .phoneNumber(signupRequest.phoneNumber())
                .password(signupRequest.password())
                .build();

        return UserInfoResponse.of(userService.save(user));
    }
}
