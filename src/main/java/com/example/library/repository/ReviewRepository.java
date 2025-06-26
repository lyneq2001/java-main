package com.example.library.repository;

import com.example.library.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByBookId(Long bookId);
    List<Review> findByUserId(Long userId);

    @Query(value = "SELECT * FROM review WHERE rating >= :rating", nativeQuery = true)
    List<Review> findByRatingGreaterThanEqual(int rating);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.book.id = :bookId")
    Double averageRating(Long bookId);

    long countByBookId(Long bookId);
}
