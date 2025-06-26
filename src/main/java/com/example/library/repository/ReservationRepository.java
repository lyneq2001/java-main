package com.example.library.repository;

import com.example.library.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByBookIdAndFulfilledFalseOrderByCreatedAtAsc(Long bookId);
}
