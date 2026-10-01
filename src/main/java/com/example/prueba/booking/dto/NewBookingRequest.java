package com.example.prueba.booking.dto;

import jakarta.validation.constraints.NotNull;

public record NewBookingRequest(@NotNull Long flightId) {
}
