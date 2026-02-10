package com.example.oauth2.model.dto.request;

import jakarta.validation.constraints.NotBlank;

public record EmployeeCreateRequest(
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        @NotBlank
        String email
) {
}
