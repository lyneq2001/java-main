package com.example.library.repository;

import com.example.library.model.Notification;
import com.example.library.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserIdAndReadFlagFalseOrderByCreatedAtDesc(Long userId);
}
