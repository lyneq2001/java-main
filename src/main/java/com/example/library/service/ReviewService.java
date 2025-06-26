package com.example.library.service;

import com.example.library.model.Review;
import com.example.library.repository.ReviewRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ReviewService {
    private final ReviewRepository repository;

    public ReviewService(ReviewRepository repository) {
        this.repository = repository;
    }

    public List<Review> findAll() { return repository.findAll(); }

    public Review findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found"));
    }

    public Review save(Review review) { return repository.save(review); }

    public Review update(Long id, Review updated) {
        Review r = findById(id);
        r.setRating(updated.getRating());
        r.setComment(updated.getComment());
        return repository.save(r);
    }

    public void delete(Long id) { repository.deleteById(id); }

    public record ReviewStats(long count, double average) {}

    public ReviewStats statistics(Long bookId) {
        long count = repository.countByBookId(bookId);
        Double avg = repository.averageRating(bookId);
        return new ReviewStats(count, avg != null ? avg : 0.0);
    }
}
