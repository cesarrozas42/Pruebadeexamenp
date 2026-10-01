package com.example.prueba.flight.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record NewFlightRequest(
        @NotBlank String airline,
        @NotBlank
        @Pattern(regexp = "^[A-Z0-9]{1,6}$", message = "must contain only A-Z and 0-9, max 6 characters")
        String flightNumber,
        @NotNull LocalDateTime departureTime,
        @NotNull LocalDateTime arrivalTime,
        @NotNull @Positive Integer availableSeats
) {
    @AssertTrue(message = "departureTime must be before arrivalTime")
    public boolean isDepartureBeforeArrival() {
        // null fields are reported by @NotNull; skip here to avoid duplicate errors
        return departureTime == null || arrivalTime == null || departureTime.isBefore(arrivalTime);
    }
}
