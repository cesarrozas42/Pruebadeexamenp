package com.example.prueba.flight.application;

import com.example.prueba.common.BadRequestException;
import com.example.prueba.common.ConflictException;
import com.example.prueba.flight.domain.Flight;
import com.example.prueba.flight.dto.FlightResponse;
import com.example.prueba.flight.dto.NewFlightRequest;
import com.example.prueba.flight.infrastructure.FlightRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
        flight.setAirlineName(request.airline());
        flight.setFlightNumber(request.flightNumber());
        flight.setEstDepartureTime(request.departureTime());
        flight.setEstArrivalTime(request.arrivalTime());
        flight.setAvailableSeats(request.availableSeats());
        return flightRepository.save(flight);
    }

    /** Every filter is optional; text filters are partial and case-insensitive. */
    @Transactional(readOnly = true)
    public List<FlightResponse> search(String flightNumber, String airline,
                                       LocalDateTime departureFrom, LocalDateTime departureTo) {
        if (departureFrom != null && departureTo != null && departureFrom.isAfter(departureTo)) {
            throw new BadRequestException("departureFrom must not be after departureTo");
        }
        Specification<Flight> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (flightNumber != null && !flightNumber.isBlank()) {
                predicates.add(cb.like(cb.upper(root.get("flightNumber")), contains(flightNumber)));
            }
            if (airline != null && !airline.isBlank()) {
                predicates.add(cb.like(cb.upper(root.get("airlineName")), contains(airline)));
            }
            if (departureFrom != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("estDepartureTime"), departureFrom));
            }
            if (departureTo != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("estDepartureTime"), departureTo));
            }
            return cb.and(predicates.toArray(Predicate[]::new));
        };
        return flightRepository.findAll(spec, Sort.by("estDepartureTime")).stream()
                .map(FlightResponse::from)
                .toList();
    }

    private static String contains(String value) {
        // escape LIKE wildcards so "%" or "_" in the input match literally
        String escaped = value.trim().toUpperCase()
                .replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
        return "%" + escaped + "%";
    }
}
