package com.codemeet.controller;

import com.codemeet.service.AuthenticationService;
import com.codemeet.utils.dto.user.UserInfoResponse;
import com.codemeet.utils.dto.user.UserLoginRequest;
import com.codemeet.utils.dto.user.UserSignupRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    
    private final AuthenticationService authService;

    @PostMapping("/login")
    public ResponseEntity<UserInfoResponse> login(
        @Valid @RequestBody UserLoginRequest loginRequest
    ) {
        System.out.println(loginRequest);
        return ResponseEntity.ok(authService.login(loginRequest));
    }
    
    @PostMapping("/signup")
    public ResponseEntity<UserInfoResponse> signup(
        @Valid @RequestBody UserSignupRequest signupRequest
    ) {
        System.out.println(signupRequest);
        return ResponseEntity.ok(authService.signup(signupRequest));
    }
}
