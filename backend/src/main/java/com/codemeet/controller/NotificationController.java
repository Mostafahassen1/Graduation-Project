package com.codemeet.controller;

import com.codemeet.service.NotificationService;
import com.codemeet.utils.dto.notification.NotificationInfoResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    private final NotificationService notificationService;
    
    @GetMapping("/{receiverId}")
    public ResponseEntity<List<NotificationInfoResponse>> getAllNotificationsByReceiverId(
        @PathVariable Integer receiverId
    ) {
        return ResponseEntity.ok(notificationService.getAllNotificationsByReceiverId(receiverId));
    }
    
    @DeleteMapping("/delete/{notificationId}")
    public ResponseEntity<Void> deleteNotificationById(
        @PathVariable Integer notificationId
    ) {
        notificationService.deleteById(notificationId);
        return ResponseEntity.noContent().build();
    }
}
