package com.example.library.repository;

import com.example.library.model.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {
    Optional<Publisher> findByName(String name);
    List<Publisher> findByNameContainingIgnoreCase(String name);

    @Query("SELECT p FROM Publisher p LEFT JOIN p.books b WHERE b.id = :bookId")
    Optional<Publisher> findByBookId(Long bookId);
}
