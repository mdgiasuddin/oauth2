package com.example.oauth2.service;

import com.example.oauth2.config.JwtService;
import com.example.oauth2.model.dto.request.LoginRequest;
import com.example.oauth2.model.dto.request.TokenRefreshRequest;
import com.example.oauth2.model.dto.response.TokenResponse;
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

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public TokenResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.username(),
                request.password()
        ));

        User user = (User) authentication.getPrincipal();
        String accessToken = jwtService.generateToken(request.username());
        String refreshToken = refreshTokenService.generateNewToken(user);
        return new TokenResponse(accessToken, refreshToken);
    }

    @Transactional
    public TokenResponse refreshAccessToken(TokenRefreshRequest request) {
        RefreshToken refreshToken = refreshTokenService.validateToken(request.refreshToken());
        User user = refreshToken.getUser();
        String accessToken = jwtService.generateToken(user.getUsername());

        return new TokenResponse(
                accessToken,
                String.format("%s-%s", refreshToken.getId().toString(), refreshToken.getRotationKey())
        );
    }
}
