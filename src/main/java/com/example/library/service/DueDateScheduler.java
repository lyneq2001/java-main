package com.example.library.service;

import com.example.library.repository.LoanRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DueDateScheduler {
    private final LoanRepository loanRepository;
    private final NotificationService notificationService;

    public DueDateScheduler(LoanRepository loanRepository, NotificationService notificationService) {
        this.loanRepository = loanRepository;
        this.notificationService = notificationService;
    }

    @Scheduled(cron = "0 0 9 * * *")
    public void checkDueDates() {
        LocalDate now = LocalDate.now().plusDays(2);
        loanRepository.findByReturnedDateIsNull().stream()
                .filter(l -> l.getDueDate() != null && !l.getDueDate().isAfter(now))
                .forEach(l -> notificationService.create(l.getUser().getId(),
                        "Loan for '" + l.getBook().getTitle() + "' is due on " + l.getDueDate()));
    }
}
