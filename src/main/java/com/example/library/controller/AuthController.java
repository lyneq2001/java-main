package com.example.library.controller;

import com.example.library.model.AppUser;
import com.example.library.model.Role;
import com.example.library.service.AppUserService;
import com.example.library.config.JwtUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthController {
    private final AppUserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(AppUserService userService, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public AppUser register(@Valid @RequestBody RegisterRequest request) {
        return userService.register(request.username, request.password, Role.USER);
    }

    @PostMapping("/login")
    public java.util.Map<String, Object> login(@Valid @RequestBody LoginRequest request) {
        try {
            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username, request.password)
            );
            if (auth.isAuthenticated()) {
                String token = jwtUtil.generateToken(request.username);
                AppUser user = userService.findByUsername(request.username);
                return java.util.Map.of("token", token, "user", user);
            }
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
    }

    @GetMapping("/session")
    public java.util.Map<String, Object> session(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return java.util.Map.of("loggedIn", false);
        }
        AppUser user = userService.findByUsername(auth.getName());
        return java.util.Map.of("loggedIn", true, "user", user);
    }

    @PostMapping("/logout")
    public void logout() {
        // With JWT auth there is no server-side session to invalidate.
    }

    public record RegisterRequest(@NotBlank String username, @NotBlank String password) {}
    public record LoginRequest(@NotBlank String username, @NotBlank String password) {}
}
