package com.example.prueba.booking.dto;

import com.example.prueba.booking.domain.Booking;
import com.example.prueba.flight.domain.Flight;

import java.time.LocalDateTime;

public record BookingResponse(
        Long id,
        LocalDateTime bookingDate,
        Long customerId,
        String customerFirstName,
        String customerLastName,
        Long flightId,
        String flightNumber,
        LocalDateTime departureTime,
        LocalDateTime arrivalTime
) {
    public static BookingResponse from(Booking booking) {
        Flight flight = booking.getFlight();
        return new BookingResponse(booking.getId(), booking.getBookingDate(), booking.getCustomer().getId(),
                booking.getCustomerFirstName(), booking.getCustomerLastName(), flight.getId(),
                flight.getFlightNumber(), flight.getEstDepartureTime(), flight.getEstArrivalTime());
    }
}
