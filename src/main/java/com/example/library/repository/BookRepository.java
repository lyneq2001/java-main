package com.example.library.repository;

import com.example.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByAuthorId(Long authorId);
    void deleteByAuthorId(Long authorId);
    List<Book> findByTitleContainingIgnoreCase(String title);
    List<Book> findByPublisherId(Long publisherId);

    @Query("SELECT b FROM Book b JOIN b.genres g WHERE g.id = :genreId ORDER BY b.title ASC")
    List<Book> findByGenreSorted(Long genreId);

    @Query("""
            SELECT DISTINCT b FROM Book b
            LEFT JOIN b.genres g
            WHERE (:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%')))
              AND (:authorId IS NULL OR b.author.id = :authorId)
              AND (:year IS NULL OR b.yearPublished = :year)
              AND (:genreId IS NULL OR g.id = :genreId)
              AND (:keyword IS NULL OR LOWER(CAST(b.description AS string)) LIKE LOWER(CONCAT('%', :keyword, '%')))
            """)
    List<Book> advancedSearch(String title, Long authorId, Integer year, Long genreId, String keyword);
}
