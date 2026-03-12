package com.example.oauth2.controller;

import com.example.oauth2.model.dto.request.LoginRequest;
import com.example.oauth2.model.dto.request.LogoutRequest;
import com.example.oauth2.model.dto.request.TokenRefreshRequest;
import com.example.oauth2.model.dto.request.UserRegistrationRequest;
import com.example.oauth2.model.dto.response.AuthResponse;
import com.example.oauth2.service.AuthenticationService;
import com.example.oauth2.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping("/register")
    public void registerUser(@Valid @RequestBody UserRegistrationRequest request) {
        userService.registerUser(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authenticationService.login(request);
    }

    @PostMapping("/refresh/access-token")
    public AuthResponse refreshAccessToken(@Valid @RequestBody TokenRefreshRequest request) {
        return authenticationService.refreshAccessToken(request);
    }

    @PostMapping("/logout")
    public void logout(@Valid @RequestBody LogoutRequest request) {
        authenticationService.logout(request);
    }
}
