package com.example.oauth2.service;

import com.example.oauth2.config.JwtService;
import com.example.oauth2.model.dto.request.LoginRequest;
import com.example.oauth2.model.dto.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.username(),
                request.password()
        ));

        String accessToken = jwtService.generateToken(request.username());
        return new LoginResponse(accessToken);
    }
}
