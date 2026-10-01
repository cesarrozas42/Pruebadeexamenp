package com.example.prueba.booking.application;

import com.example.prueba.booking.dto.BookingResponse;
import com.example.prueba.booking.dto.NewBookingRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    // principal is the email set by JwtAuthenticationFilter
    @PostMapping("/flights/book")
    public ResponseEntity<BookingResponse> book(@AuthenticationPrincipal String email,
                                                @Valid @RequestBody NewBookingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.book(email, request));
    }

    @GetMapping("/flight/book/{id}")
    public ResponseEntity<BookingResponse> findById(@AuthenticationPrincipal String email, @PathVariable Long id) {
        return ResponseEntity.ok(bookingService.findById(email, id));
    }
}
