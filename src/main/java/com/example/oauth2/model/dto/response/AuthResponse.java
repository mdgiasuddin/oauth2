package com.example.oauth2.model.dto.response;

public record AuthResponse(
        String name,
        String accessToken,
        String refreshToken
) {
}
