package com.example.prueba.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RegisterUserRequest(
        @NotBlank
        @Pattern(regexp = ".*[A-Z].*", message = "must contain at least 1 uppercase letter")
        String firstName,
        @NotBlank
        @Pattern(regexp = ".*[A-Z].*", message = "must contain at least 1 uppercase letter")
        String lastName,
        // default @Email accepts "a@b"; require a domain with a dot
        @NotBlank @Email(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
        String email,
        @NotBlank
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).{8,}$",
                message = "must be at least 8 characters with at least 1 letter and 1 number")
        String password
) {
}
