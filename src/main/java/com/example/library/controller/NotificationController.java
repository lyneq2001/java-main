package com.example.library.controller;

import com.example.library.model.Notification;
import com.example.library.service.NotificationService;
import com.example.library.service.AppUserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationService service;
    private final AppUserService userService;

    public NotificationController(NotificationService service, AppUserService userService) {
        this.service = service;
        this.userService = userService;
    }

    @GetMapping
    public List<Notification> unread(Authentication auth) {
        Long userId = userService.findByUsername(auth.getName()).getId();
        return service.unread(userId);
    }

    @PutMapping("/{id}/read")
    public void read(Authentication auth, @PathVariable Long id) {
        Long userId = userService.findByUsername(auth.getName()).getId();
        service.markRead(id, userId);
    }
}
