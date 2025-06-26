package com.example.library.controller;

import com.example.library.model.AppUser;
import com.example.library.model.Loan;
import com.example.library.model.Role;
import com.example.library.service.AppUserService;
import com.example.library.service.LoanService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loans")
public class LoanController {
    private final LoanService loanService;
    private final AppUserService userService;

    public LoanController(LoanService loanService, AppUserService userService) {
        this.loanService = loanService;
        this.userService = userService;
    }

    @GetMapping
    public List<Loan> getLoans(Authentication auth) {
        AppUser user = userService.findByUsername(auth.getName());
        if (user.getRole() == Role.ADMIN) {
            return loanService.findAll();
        }
        return loanService.findActiveByUser(user.getId());
    }

    @GetMapping("/my")
    public List<Loan> myLoans(Authentication auth) {
        AppUser user = userService.findByUsername(auth.getName());
        return loanService.findActiveByUser(user.getId());
    }

    public record LoanRequest(Long bookId) {}

    @PostMapping
    public Loan borrow(Authentication auth, @RequestBody LoanRequest request) {
        AppUser user = userService.findByUsername(auth.getName());
        return loanService.borrowBook(user.getId(), request.bookId());
    }

    @PutMapping("/{id}/return")
    public void returnBook(Authentication auth, @PathVariable Long id) {
        AppUser user = userService.findByUsername(auth.getName());
        Loan loan = loanService.findById(id);
        if (!loan.getUser().getId().equals(user.getId()) && user.getRole() != Role.ADMIN) {
            throw new RuntimeException("Forbidden");
        }
        loanService.returnLoan(id);
    }
}
