package com.example.library.repository;

import com.example.library.model.AppUser;
import com.example.library.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
    List<AppUser> findByRole(Role role);
    List<AppUser> findByUsernameContainingIgnoreCase(String username);

    Optional<AppUser> findFirstByUsernameOrderByIdDesc(String username);
}
