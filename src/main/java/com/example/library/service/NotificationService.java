package com.example.library.service;

import com.example.library.model.Notification;
import com.example.library.model.AppUser;
import com.example.library.repository.NotificationRepository;
import com.example.library.repository.AppUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificationService {
    private final NotificationRepository repository;
    private final AppUserRepository userRepository;

    public NotificationService(NotificationRepository repository, AppUserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public Notification create(Long userId, String message) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return repository.save(new Notification(user, message));
    }

    public List<Notification> unread(Long userId) {
        return repository.findByUserIdAndReadFlagFalseOrderByCreatedAtDesc(userId);
    }

    @Transactional
    public void markRead(Long id, Long userId) {
        Notification n = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        if (!n.getUser().getId().equals(userId)) {
            throw new RuntimeException("Forbidden");
        }
        n.setReadFlag(true);
    }
}
