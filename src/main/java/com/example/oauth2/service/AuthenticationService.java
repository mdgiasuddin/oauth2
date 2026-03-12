package com.example.oauth2.service;

import com.example.oauth2.config.JwtService;
import com.example.oauth2.model.dto.request.LoginRequest;
import com.example.oauth2.model.dto.request.LogoutRequest;
import com.example.oauth2.model.dto.request.TokenRefreshRequest;
import com.example.oauth2.model.dto.response.AuthResponse;
import com.example.oauth2.model.entity.RefreshToken;
import com.example.oauth2.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.username(),
                request.password()
        ));

        User user = (User) authentication.getPrincipal();
        String accessToken = jwtService.generateToken(request.username());
        String refreshToken = refreshTokenService.generateNewToken(user);
        return new AuthResponse(user.getName(), accessToken, refreshToken);
    }

    @Transactional
    public AuthResponse refreshAccessToken(TokenRefreshRequest request) {
        RefreshToken refreshToken = refreshTokenService.validateToken(request.refreshToken());
        User user = refreshToken.getUser();
        String accessToken = jwtService.generateToken(user.getUsername());

        return new AuthResponse(
                user.getName(),
                accessToken,
                String.format("%s-%s", refreshToken.getId().toString(), refreshToken.getRotationKey())
        );
    }

    public void logout(LogoutRequest request) {
        refreshTokenService.removeRefreshToken(request.refreshToken());
    }
}
