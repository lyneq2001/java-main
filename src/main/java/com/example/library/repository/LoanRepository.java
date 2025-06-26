package com.example.library.repository;

import com.example.library.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByUserId(Long userId);
    List<Loan> findByBookId(Long bookId);
    List<Loan> findByUserIdAndReturnedDateIsNull(Long userId);
    List<Loan> findByBookIdAndReturnedDateIsNull(Long bookId);
    @Query("SELECT l FROM Loan l WHERE l.user.username = :username")
    List<Loan> findByUsername(String username);
    Optional<Loan> findFirstByBookIdOrderByBorrowedAtDesc(Long bookId);
    Optional<Loan> findFirstByBookIdAndReturnedDateIsNullOrderByDueDateAsc(Long bookId);

    List<Loan> findByReturnedDateIsNull();
}
