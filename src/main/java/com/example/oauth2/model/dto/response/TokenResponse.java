package com.example.oauth2.model.dto.response;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {
}
