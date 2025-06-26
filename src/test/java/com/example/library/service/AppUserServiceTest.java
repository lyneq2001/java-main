package com.example.library.service;

import com.example.library.model.AppUser;
import com.example.library.model.Role;
import com.example.library.repository.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppUserServiceTest {

    @Mock
    private AppUserRepository repository;
    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private AppUserService service;

    @Test
    void updateThrowsWhenUsernameExists() {
        AppUser existing = new AppUser("other", "x", Role.USER);
        existing.setId(2L);

        AppUser current = new AppUser("old", "x", Role.USER);
        current.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(current));
        when(repository.findByUsername("other")).thenReturn(Optional.of(existing));

        AppUser updated = new AppUser();
        updated.setUsername("other");

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> service.update(1L, updated));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        verify(repository, never()).save(any());
    }

    @Test
    void updateUsernameWhenAvailableSaves() {
        AppUser current = new AppUser("old", "x", Role.USER);
        current.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(current));
        when(repository.findByUsername("new")).thenReturn(Optional.empty());
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        AppUser updated = new AppUser();
        updated.setUsername("new");

        AppUser result = service.update(1L, updated);
        assertEquals("new", result.getUsername());
        verify(repository).save(current);
    }
}
