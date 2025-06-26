package com.example.library.controller;

import com.example.library.model.AppUser;
import com.example.library.model.Role;
import com.example.library.service.AppUserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final AppUserService userService;

    public UserController(AppUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public AppUser me(Authentication auth) {
        return userService.findByUsername(auth.getName());
    }

    @PutMapping("/me")
    public AppUser updateMe(Authentication auth, @RequestBody AppUser updated) {
        AppUser current = userService.findByUsername(auth.getName());
        return userService.update(current.getId(), updated);
    }

    @GetMapping
    public List<AppUser> all(Authentication auth) {
        AppUser current = userService.findByUsername(auth.getName());
        if (current.getRole() != Role.ADMIN) {
            throw new RuntimeException("Forbidden");
        }
        return userService.findAll();
    }

    @PutMapping("/{id}")
    public AppUser update(Authentication auth, @PathVariable Long id, @RequestBody AppUser updated) {
        AppUser current = userService.findByUsername(auth.getName());
        if (current.getRole() != Role.ADMIN) {
            throw new RuntimeException("Forbidden");
        }
        return userService.update(id, updated);
    }

    @DeleteMapping("/{id}")
    public void delete(Authentication auth, @PathVariable Long id) {
        AppUser current = userService.findByUsername(auth.getName());
        if (current.getRole() != Role.ADMIN) {
            throw new RuntimeException("Forbidden");
        }
        userService.delete(id);
    }
}
