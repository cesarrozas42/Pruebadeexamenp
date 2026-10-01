package com.example.prueba.booking.application;

import com.example.prueba.booking.domain.Booking;
import com.example.prueba.booking.domain.BookingConfirmedEvent;
import com.example.prueba.booking.dto.BookingResponse;
import com.example.prueba.booking.dto.NewBookingRequest;
import com.example.prueba.booking.infrastructure.BookingRepository;
import com.example.prueba.common.BadRequestException;
import com.example.prueba.common.ConflictException;
import com.example.prueba.common.NotFoundException;
import com.example.prueba.common.UnauthorizedException;
import com.example.prueba.flight.domain.Flight;
import com.example.prueba.flight.infrastructure.FlightRepository;
import com.example.prueba.user.domain.User;
import com.example.prueba.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final FlightRepository flightRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public BookingResponse book(String email, NewBookingRequest request) {
        User customer = findUser(email);
        Flight flight = flightRepository.findByIdForUpdate(request.flightId())
                .orElseThrow(() -> new NotFoundException("Flight " + request.flightId() + " not found"));

        LocalDateTime now = LocalDateTime.now();
        // a flight whose departure has passed is either in transit or already landed
        if (!flight.getEstDepartureTime().isAfter(now)) {
            throw new BadRequestException("Flight " + flight.getFlightNumber() + " has already departed");
        }
        if (flight.getAvailableSeats() <= 0) {
            throw new ConflictException("Flight " + flight.getFlightNumber() + " has no available seats");
        }
        if (bookingRepository.existsOverlapping(customer.getId(),
                flight.getEstDepartureTime(), flight.getEstArrivalTime())) {
            throw new ConflictException("You already have a booking that overlaps with flight " + flight.getFlightNumber());
        }

        flight.setAvailableSeats(flight.getAvailableSeats() - 1);

        Booking booking = new Booking();
        booking.setFlight(flight);
        booking.setCustomer(customer);
        booking.setCustomerFirstName(customer.getFirstName());
        booking.setCustomerLastName(customer.getLastName());
        booking.setBookingDate(now);
        booking = bookingRepository.save(booking);

        eventPublisher.publishEvent(BookingConfirmedEvent.from(booking));
        return BookingResponse.from(booking);
    }

    @Transactional(readOnly = true)
    public BookingResponse findById(String email, Long id) {
        User customer = findUser(email);
        Booking booking = bookingRepository.findById(id)
                // other customers' bookings are reported as missing so ids can't be probed
                .filter(b -> b.getCustomer().getId().equals(customer.getId()))
                .orElseThrow(() -> new NotFoundException("Booking " + id + " not found"));
        return BookingResponse.from(booking);
    }

    private User findUser(String email) {
        // a still-valid token can outlive its account
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Authenticated user no longer exists"));
    }
}
