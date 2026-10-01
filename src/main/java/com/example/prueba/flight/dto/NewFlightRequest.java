package com.example.prueba.flight.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record NewFlightRequest(
        @NotBlank String airlineName,
        @NotBlank
        @Pattern(regexp = "^[A-Z0-9]{1,6}$", message = "must contain only A-Z and 0-9, max 6 characters")
        String flightNumber,
        @NotNull LocalDateTime estDepartureTime,
        @NotNull LocalDateTime estArrivalTime,
        @NotNull @Positive Integer availableSeats
) {
    @AssertTrue(message = "estDepartureTime must be before estArrivalTime")
    public boolean isDepartureBeforeArrival() {
        // null fields are reported by @NotNull; skip here to avoid duplicate errors
        return estDepartureTime == null || estArrivalTime == null || estDepartureTime.isBefore(estArrivalTime);
    }
}
