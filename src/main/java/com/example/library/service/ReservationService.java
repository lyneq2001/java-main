package com.example.library.service;

import com.example.library.model.Book;
import com.example.library.model.Reservation;
import com.example.library.model.AppUser;
import com.example.library.repository.ReservationRepository;
import com.example.library.repository.BookRepository;
import com.example.library.repository.AppUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository repository;
    private final BookRepository bookRepository;
    private final AppUserRepository userRepository;
    private final NotificationService notificationService;

    public ReservationService(ReservationRepository repository,
                              BookRepository bookRepository,
                              AppUserRepository userRepository,
                              NotificationService notificationService) {
        this.repository = repository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    public Reservation create(Long userId, Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Reservation r = new Reservation(book, user);
        return repository.save(r);
    }

    public List<Reservation> forBook(Long bookId) {
        return repository.findByBookIdAndFulfilledFalseOrderByCreatedAtAsc(bookId);
    }

    @Transactional
    public void notifyNext(Long bookId) {
        List<Reservation> list = repository.findByBookIdAndFulfilledFalseOrderByCreatedAtAsc(bookId);
        if (!list.isEmpty()) {
            Reservation r = list.get(0);
            r.setFulfilled(true);
            notificationService.create(r.getUser().getId(),
                    "Book '" + r.getBook().getTitle() + "' is now available");
        }
    }
}
