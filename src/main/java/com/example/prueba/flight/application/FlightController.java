package com.example.prueba.flight.application;

import com.example.prueba.common.IdResponse;
import com.example.prueba.flight.dto.FlightResponse;
import com.example.prueba.flight.dto.NewFlightRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/flights")
@RequiredArgsConstructor
public class FlightController {

    private final FlightService flightService;

    @PostMapping("/create")
    public ResponseEntity<IdResponse> create(@Valid @RequestBody NewFlightRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new IdResponse(flightService.create(request).getId()));
    }

    @GetMapping("/search")
    public ResponseEntity<List<FlightResponse>> search(
            @RequestParam(required = false) String flightNumber,
            @RequestParam(required = false) String airline,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime departureFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime departureTo) {
        return ResponseEntity.ok(flightService.search(flightNumber, airline, departureFrom, departureTo));
    }
}
