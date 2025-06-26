package com.example.library.service;

import com.example.library.model.Genre;
import com.example.library.repository.GenreRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class GenreService {
    private final GenreRepository repository;

    public GenreService(GenreRepository repository) {
        this.repository = repository;
    }

    public List<Genre> findAll() { return repository.findAll(); }

    public Genre findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Genre not found"));
    }

    public Genre save(Genre genre) { return repository.save(genre); }

    public Genre update(Long id, Genre updated) {
        Genre g = findById(id);
        g.setName(updated.getName());
        return repository.save(g);
    }

    public void delete(Long id) { repository.deleteById(id); }
}
