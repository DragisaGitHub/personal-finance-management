package com.dragi.finance_manager.notification;

import com.dragi.finance_manager.util.HelperUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public ResponseEntity<List<Notification>> getNotificationsForUser() {
        String username = HelperUtils.getAuthenticatedUsername();
        List<Notification> notifications = notificationService.getUnreadNotificationsForUser(username);
        return ResponseEntity.ok(notifications);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> markAsRead(@PathVariable Long id) {
        notificationService.markNotificationAsRead(id);
        return ResponseEntity.ok().build();
    }
}