package com.example.prueba.booking.application;

import com.example.prueba.booking.domain.BookingConfirmedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class BookingEmailListener {

    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private final Path outputDir;

    public BookingEmailListener(@Value("${booking.email-dir}") String outputDir) {
        this.outputDir = Path.of(outputDir);
    }

    // AFTER_COMMIT (default phase): no email for a booking that was rolled back
    @TransactionalEventListener
    public void onBookingConfirmed(BookingConfirmedEvent event) {
        Path file = outputDir.resolve("flight_booking_email_" + event.bookingId() + ".txt");
        String passenger = event.customerFirstName() + " " + event.customerLastName();
        String body = """
                To: %s
                Subject: Fly Away Travel - Booking confirmation #%d

                Hello %s,

                Your booking has been confirmed.

                Booking ID:     %d
                Passenger:      %s
                Flight number:  %s
                Airline:        %s
                Departure:      %s
                Arrival:        %s
                Booking date:   %s

                Thank you for flying with Fly Away Travel!
                """.formatted(
                passenger, event.bookingId(),
                passenger,
                event.bookingId(),
                passenger,
                event.flightNumber(),
                event.airline(),
                ISO.format(event.departureTime()),
                ISO.format(event.arrivalTime()),
                ISO.format(event.bookingDate()));
        try {
            Files.createDirectories(outputDir);
            Files.writeString(file, body, StandardCharsets.UTF_8);
            log.info("Booking confirmation email written to {}", file.toAbsolutePath());
        } catch (IOException e) {
            // the booking is already committed; a failed email must not turn it into an error response
            log.error("Could not write booking confirmation email {}", file.toAbsolutePath(), e);
        }
    }
}
