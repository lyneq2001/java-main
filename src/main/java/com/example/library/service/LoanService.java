package com.example.library.service;

import com.example.library.model.AppUser;
import com.example.library.model.Book;
import com.example.library.model.Loan;
import com.example.library.repository.AppUserRepository;
import com.example.library.repository.BookRepository;
import com.example.library.repository.LoanRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class LoanService {
    private final LoanRepository loanRepository;
    private final AppUserRepository userRepository;
    private final BookRepository bookRepository;
    private final ReservationService reservationService;

    public LoanService(LoanRepository loanRepository,
                       AppUserRepository userRepository,
                       BookRepository bookRepository,
                       ReservationService reservationService) {
        this.loanRepository = loanRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.reservationService = reservationService;
    }

    public List<Loan> findAll() { return loanRepository.findAll(); }

    public List<Loan> findActiveByUser(Long userId) {
        return loanRepository.findByUserIdAndReturnedDateIsNull(userId);
    }

    @Transactional
    public Loan borrowBook(Long userId, Long bookId) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));
        int active = loanRepository.findByBookIdAndReturnedDateIsNull(bookId).size();
        if (book.getCopies() != null && active >= book.getCopies()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Book already loaned");
        }
        Loan loan = new Loan(book, user);
        return loanRepository.save(loan);
    }

    @Transactional
    public void returnLoan(Long loanId) {
        Loan loan = findById(loanId);
        loan.setReturnedDate(java.time.LocalDate.now());
        loanRepository.save(loan);
        reservationService.notifyNext(loan.getBook().getId());
    }

    public Loan findById(Long id) {
        return loanRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Loan not found"));
    }
}
