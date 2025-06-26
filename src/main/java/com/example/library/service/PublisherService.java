package com.example.library.service;

import com.example.library.model.Publisher;
import com.example.library.repository.PublisherRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PublisherService {
    private final PublisherRepository repository;

    public PublisherService(PublisherRepository repository) {
        this.repository = repository;
    }

    public List<Publisher> findAll() {
        return repository.findAll();
    }

    public Publisher findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Publisher not found"));
    }

    public Publisher save(Publisher publisher) {
        return repository.save(publisher);
    }

    public Publisher update(Long id, Publisher updated) {
        Publisher p = findById(id);
        p.setName(updated.getName());
        return repository.save(p);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
