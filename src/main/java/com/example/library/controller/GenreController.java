package com.example.library.controller;

import com.example.library.model.Genre;
import com.example.library.service.GenreService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/genres")
public class GenreController {
    private final GenreService service;

    public GenreController(GenreService service) { this.service = service; }

    @GetMapping
    public List<Genre> all() { return service.findAll(); }

    @PostMapping
    public Genre create(@Valid @RequestBody Genre genre) { return service.save(genre); }

    @GetMapping("/{id}")
    public Genre get(@PathVariable Long id) { return service.findById(id); }

    @PutMapping("/{id}")
    public Genre update(@PathVariable Long id, @Valid @RequestBody Genre genre) { return service.update(id, genre); }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { service.delete(id); }
}
