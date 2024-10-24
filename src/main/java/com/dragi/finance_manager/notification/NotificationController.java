package com.dragi.finance_manager.notification;

import com.dragi.finance_manager.util.HelperUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@Tag(name = "Notifications", description = "API for managing notifications for users.")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    @Operation(summary = "Get unread notifications", description = "Retrieve unread notifications for the authenticated user.")
    public ResponseEntity<List<Notification>> getNotificationsForUser() {
        String username = HelperUtils.getAuthenticatedUsername();
        List<Notification> notifications = notificationService.getUnreadNotificationsForUser(username);
        return ResponseEntity.ok(notifications);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mark notification as read", description = "Mark a specific notification as read by ID.")
    public ResponseEntity<?> markAsRead(@PathVariable Long id) {
        notificationService.markNotificationAsRead(id);
        return ResponseEntity.ok().build();
    }
}