package com.example.library.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    private Book book;

    @ManyToOne
    @NotNull
    private AppUser user;

    private LocalDate borrowedAt = LocalDate.now();

    private LocalDate dueDate = borrowedAt.plusWeeks(2);

    private LocalDate returnedDate;

    public Loan() {}

    public Loan(Book book, AppUser user) {
        this.book = book;
        this.user = user;
        this.borrowedAt = LocalDate.now();
        this.dueDate = borrowedAt.plusWeeks(2);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }

    public AppUser getUser() { return user; }
    public void setUser(AppUser user) { this.user = user; }

    public LocalDate getBorrowedAt() { return borrowedAt; }
    public void setBorrowedAt(LocalDate borrowedAt) { this.borrowedAt = borrowedAt; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public LocalDate getReturnedDate() { return returnedDate; }
    public void setReturnedDate(LocalDate returnedDate) { this.returnedDate = returnedDate; }
}
