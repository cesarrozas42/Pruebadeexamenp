package com.example.prueba.flight.application;

import com.example.prueba.common.IdResponse;
import com.example.prueba.flight.dto.NewFlightRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
