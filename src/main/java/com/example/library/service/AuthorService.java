package com.example.library.service;

import com.example.library.model.Author;
import com.example.library.repository.AuthorRepository;
import com.example.library.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public AuthorService(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    public List<Author> searchByName(String part) {
        return authorRepository.searchByName(part);
    }


    public Author findById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found"));
    }

    public Author save(Author author) {
        return authorRepository.save(author);
    }

    @Transactional
    public Author update(Long id, Author updated) {
        Author author = findById(id);
        if (updated.getFirstName() != null) {
            author.setFirstName(updated.getFirstName());
        }
        if (updated.getLastName() != null) {
            author.setLastName(updated.getLastName());
        }
        if (updated.getYearOfBirth() != null) {
            author.setYearOfBirth(updated.getYearOfBirth());
        }
        if (updated.getDescription() != null) {
            author.setDescription(updated.getDescription());
        }
        if (updated.getNationality() != null) {
            author.setNationality(updated.getNationality());
        }
        if (updated.getYearOfDeath() != null) {
            author.setYearOfDeath(updated.getYearOfDeath());
        }
        if (updated.getWebsite() != null) {
            author.setWebsite(updated.getWebsite());
        }
        if (updated.getPhotoUrl() != null) {
            author.setPhotoUrl(updated.getPhotoUrl());
        }
        return authorRepository.save(author);
    }

    @Transactional
    public void delete(Long id) {
        bookRepository.deleteByAuthorId(id);
        authorRepository.deleteById(id);
    }
}
