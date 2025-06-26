package com.example.library.controller;

import com.example.library.model.Publisher;
import com.example.library.service.PublisherService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/publishers")
public class PublisherController {
    private final PublisherService service;

    public PublisherController(PublisherService service) {
        this.service = service;
    }

    @GetMapping
    public List<Publisher> all() { return service.findAll(); }

    @PostMapping
    public Publisher create(@Valid @RequestBody Publisher publisher) { return service.save(publisher); }

    @GetMapping("/{id}")
    public Publisher get(@PathVariable Long id) { return service.findById(id); }

    @PutMapping("/{id}")
    public Publisher update(@PathVariable Long id, @Valid @RequestBody Publisher publisher) { return service.update(id, publisher); }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { service.delete(id); }
}
