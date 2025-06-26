package com.example.library.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(1)
    @Max(5)
    private int rating;

    private String comment;

    @ManyToOne
    @NotNull
    private Book book;

    @ManyToOne
    @NotNull
    private AppUser user;

    public Review() {}

    public Review(int rating, String comment, Book book, AppUser user) {
        this.rating = rating;
        this.comment = comment;
        this.book = book;
        this.user = user;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }

    public AppUser getUser() { return user; }
    public void setUser(AppUser user) { this.user = user; }
}
