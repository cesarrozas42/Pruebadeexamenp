package com.example.prueba.flight.dto;

import com.example.prueba.flight.domain.Flight;

import java.time.LocalDateTime;

public record FlightResponse(
        Long id,
        String flightNumber,
        String airline,
        LocalDateTime departureTime,
        LocalDateTime arrivalTime,
        Integer availableSeats
) {
    public static FlightResponse from(Flight flight) {
        return new FlightResponse(flight.getId(), flight.getFlightNumber(), flight.getAirlineName(),
                flight.getEstDepartureTime(), flight.getEstArrivalTime(), flight.getAvailableSeats());
    }
}
