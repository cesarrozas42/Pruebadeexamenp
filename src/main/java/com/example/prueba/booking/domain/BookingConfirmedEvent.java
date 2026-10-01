package com.example.prueba.booking.domain;

import java.time.LocalDateTime;

/** Snapshot of a booking, published so the confirmation email is written only after the transaction commits. */
public record BookingConfirmedEvent(
        Long bookingId,
        String customerFirstName,
        String customerLastName,
        String flightNumber,
        String airline,
        LocalDateTime departureTime,
        LocalDateTime arrivalTime,
        LocalDateTime bookingDate
) {
    public static BookingConfirmedEvent from(Booking booking) {
        return new BookingConfirmedEvent(booking.getId(), booking.getCustomerFirstName(),
                booking.getCustomerLastName(), booking.getFlight().getFlightNumber(),
                booking.getFlight().getAirlineName(), booking.getFlight().getEstDepartureTime(),
                booking.getFlight().getEstArrivalTime(), booking.getBookingDate());
    }
}
