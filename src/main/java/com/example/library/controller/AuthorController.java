package com.example.library.controller;

import com.example.library.model.Author;
import com.example.library.service.AuthorService;
import com.example.library.service.AppUserService;
import com.example.library.model.Role;
import org.springframework.security.core.Authentication;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/authors")
public class AuthorController {
    private final AuthorService authorService;
    private final AppUserService userService;

    public AuthorController(AuthorService authorService, AppUserService userService) {
        this.authorService = authorService;
        this.userService = userService;
    }

    @GetMapping
    public List<Author> getAll() {
        return authorService.findAll();
    }

    @GetMapping("/search")
    public List<Author> search(@RequestParam String name) {
        return authorService.searchByName(name);
    }

    @PostMapping
    public Author create(@Valid @RequestBody Author author) {
        return authorService.save(author);
    }

    @GetMapping("/{id}")
    public Author getById(@PathVariable Long id) {
        return authorService.findById(id);
    }

    @PutMapping("/{id}")
    public Author update(Authentication auth, @PathVariable Long id, @Valid @RequestBody Author author) {
        if (userService.findByUsername(auth.getName()).getRole() != Role.ADMIN) {
            throw new RuntimeException("Forbidden");
        }
        return authorService.update(id, author);
    }

    @DeleteMapping("/{id}")
    public void delete(Authentication auth, @PathVariable Long id) {
        if (userService.findByUsername(auth.getName()).getRole() != Role.ADMIN) {
            throw new RuntimeException("Forbidden");
        }
        authorService.delete(id);
    }
}
