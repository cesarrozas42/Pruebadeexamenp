package com.example.prueba.booking.infrastructure;

import com.example.prueba.booking.domain.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    /** True if the customer already holds a booking whose flight overlaps the [departure, arrival) window. */
    @Query("""
            select count(b) > 0 from Booking b
            where b.customer.id = :customerId
              and b.flight.estDepartureTime < :arrival
              and b.flight.estArrivalTime > :departure
            """)
    boolean existsOverlapping(Long customerId, LocalDateTime departure, LocalDateTime arrival);
}
