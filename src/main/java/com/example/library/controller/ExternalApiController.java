package com.example.library.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/external")
public class ExternalApiController {
    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/books")
    public Object getBooks() {
        return restTemplate.getForObject("http://localhost:8080/books", Object.class);
    }

    @PostMapping("/authors")
    public ResponseEntity<?> createAuthor(@RequestBody Object author) {
        return restTemplate.postForEntity("http://localhost:8080/authors", author, Object.class);
    }

    @DeleteMapping("/books/{id}")
    public void deleteBook(@PathVariable Long id) {
        restTemplate.delete("http://localhost:8080/books/" + id);
    }
}
