package com.example.library.controller;

import com.example.library.model.Review;
import com.example.library.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService service;

    public ReviewController(ReviewService service) { this.service = service; }

    @GetMapping
    public List<Review> all() { return service.findAll(); }

    @PostMapping
    public Review create(@Valid @RequestBody Review review) { return service.save(review); }

    @GetMapping("/{id}")
    public Review get(@PathVariable Long id) { return service.findById(id); }

    @PutMapping("/{id}")
    public Review update(@PathVariable Long id, @Valid @RequestBody Review review) { return service.update(id, review); }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { service.delete(id); }

    @GetMapping("/stats/{bookId}")
    public ReviewService.ReviewStats stats(@PathVariable Long bookId) {
        return service.statistics(bookId);
    }
}
