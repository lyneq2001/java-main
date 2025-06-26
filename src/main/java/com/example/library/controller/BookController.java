package com.example.library.controller;

import com.example.library.model.Book;
import com.example.library.service.BookService;
import com.example.library.service.AppUserService;
import com.example.library.model.Role;
import org.springframework.security.core.Authentication;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;
    private final AppUserService userService;

    public BookController(BookService bookService, AppUserService userService) {
        this.bookService = bookService;
        this.userService = userService;
    }

    @GetMapping
    public List<Book> getAll() {
        return bookService.findAll();
    }

    @GetMapping("/public")
    public List<BookService.PublicBook> getPublic() {
        return bookService.findPublicBooks();
    }

    @GetMapping("/{id}/status")
    public BookService.PublicBook status(@PathVariable Long id) {
        return bookService.getPublicBook(id);
    }

    @PostMapping
    public Book create(@Valid @RequestBody Book book) {
        return bookService.save(book);
    }

    @GetMapping("/{id}")
    public Book getById(@PathVariable Long id) {
        return bookService.findById(id);
    }

    @PutMapping("/{id}")
    public Book update(Authentication auth, @PathVariable Long id, @Valid @RequestBody Book book) {
        if (userService.findByUsername(auth.getName()).getRole() != Role.ADMIN) {
            throw new RuntimeException("Forbidden");
        }
        return bookService.update(id, book);
    }

    @DeleteMapping("/{id}")
    public void delete(Authentication auth, @PathVariable Long id) {
        if (userService.findByUsername(auth.getName()).getRole() != Role.ADMIN) {
            throw new RuntimeException("Forbidden");
        }
        bookService.delete(id);
    }

    @GetMapping("/author/{authorId}")
    public List<Book> getByAuthor(@PathVariable Long authorId) {
        return bookService.findByAuthor(authorId);
    }

    @GetMapping("/genre/{genreId}")
    public List<Book> getByGenreSorted(@PathVariable Long genreId) {
        return bookService.findByGenreSorted(genreId);
    }

    @GetMapping("/search")
    public List<Book> search(@RequestParam String title) {
        return bookService.searchByTitle(title);
    }

    @GetMapping("/advanced")
    public List<Book> advanced(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Long authorId,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Long genreId,
            @RequestParam(required = false) String keyword) {
        return bookService.advancedSearch(title, authorId, year, genreId, keyword);
    }
}
