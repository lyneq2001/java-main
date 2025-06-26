package com.example.library.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api2")
public class ApiTwoController {
    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/books")
    public Object fetchBooks() {
        return restTemplate.getForObject("http://localhost:8080/external/books", Object.class);
    }

    @PostMapping("/authors")
    public ResponseEntity<?> sendAuthor(@RequestBody Object author) {
        return restTemplate.postForEntity("http://localhost:8080/external/authors", author, Object.class);
    }

    @DeleteMapping("/books/{id}")
    public void removeBook(@PathVariable Long id) {
        restTemplate.delete("http://localhost:8080/external/books/" + id);
    }
}
