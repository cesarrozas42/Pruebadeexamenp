package com.example.prueba.flight.application;

import com.example.prueba.common.ConflictException;
import com.example.prueba.flight.domain.Flight;
import com.example.prueba.flight.dto.NewFlightRequest;
import com.example.prueba.flight.infrastructure.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FlightService {

    private final FlightRepository flightRepository;

    @Transactional
    public Flight create(NewFlightRequest request) {
        if (flightRepository.existsByFlightNumber(request.flightNumber())) {
            throw new ConflictException("Flight number " + request.flightNumber() + " already exists");
        }
        Flight flight = new Flight();
        flight.setAirlineName(request.airlineName());
        flight.setFlightNumber(request.flightNumber());
        flight.setEstDepartureTime(request.estDepartureTime());
        flight.setEstArrivalTime(request.estArrivalTime());
        flight.setAvailableSeats(request.availableSeats());
        return flightRepository.save(flight);
    }
}
