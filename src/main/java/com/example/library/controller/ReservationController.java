package com.example.library.controller;

import com.example.library.model.Reservation;
import com.example.library.service.ReservationService;
import com.example.library.service.AppUserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService service;
    private final AppUserService userService;

    public ReservationController(ReservationService service, AppUserService userService) {
        this.service = service;
        this.userService = userService;
    }

    public record Request(Long bookId) {}

    @PostMapping
    public Reservation create(Authentication auth, @RequestBody Request req) {
        Long userId = userService.findByUsername(auth.getName()).getId();
        return service.create(userId, req.bookId());
    }

    @GetMapping("/book/{bookId}")
    public List<Reservation> forBook(@PathVariable Long bookId) {
        return service.forBook(bookId);
    }
}
