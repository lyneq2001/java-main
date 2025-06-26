package com.example.library.repository;

import com.example.library.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    Optional<Genre> findByNameIgnoreCase(String name);
    List<Genre> findByBooksId(Long bookId);

    @Query("SELECT g FROM Genre g JOIN g.books b WHERE b.author.id = :authorId")
    List<Genre> findByAuthorId(Long authorId);
}
