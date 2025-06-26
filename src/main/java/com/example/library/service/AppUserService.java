package com.example.library.service;

import com.example.library.model.AppUser;
import com.example.library.model.Role;
import com.example.library.repository.AppUserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AppUserService implements UserDetailsService {
    private final AppUserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public AppUserService(AppUserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public AppUser register(String username, String password, Role role) {
        if (repository.findByUsername(username).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists");
        }
        AppUser user = new AppUser(username, passwordEncoder.encode(password), role);
        return repository.save(user);
    }

    public AppUser findByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public AppUser findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public java.util.List<AppUser> findAll() {
        return repository.findAll();
    }

    public AppUser update(Long id, AppUser updated) {
        AppUser user = findById(id);
        if (updated.getUsername() != null) {
            String newUsername = updated.getUsername();
            if (!newUsername.equals(user.getUsername())) {
                repository.findByUsername(newUsername)
                        .filter(u -> !u.getId().equals(id))
                        .ifPresent(u -> {
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists");
                        });
            }
            user.setUsername(newUsername);
        }
        if (updated.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(updated.getPassword()));
        }
        if (updated.getRole() != null) {
            user.setRole(updated.getRole());
        }
        return repository.save(user);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return User.withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }
}
