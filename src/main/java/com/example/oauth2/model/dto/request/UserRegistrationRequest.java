package com.example.oauth2.model.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UserRegistrationRequest(
        @NotBlank
        String name,
        @NotBlank
        String username,
        @NotBlank
        String password
) {
}
