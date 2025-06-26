package com.example.library.repository;

import com.example.library.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Query("SELECT a FROM Author a WHERE lower(concat(a.firstName, ' ', a.lastName)) = lower(:name)")
    Optional<Author> findByFullName(String name);

    @Query("SELECT a FROM Author a WHERE lower(concat(a.firstName, ' ', a.lastName)) LIKE lower(concat('%', :part, '%'))")
    List<Author> searchByName(String part);

    @Query("SELECT a FROM Author a WHERE LENGTH(concat(a.firstName, ' ', a.lastName)) > :length")
    List<Author> findWithNameLongerThan(int length);
}
