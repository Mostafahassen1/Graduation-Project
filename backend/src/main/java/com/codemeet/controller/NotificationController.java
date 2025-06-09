package com.codemeet.controller;

import com.codemeet.entity.NotificationType;
import com.codemeet.service.NotificationService;
import com.codemeet.utils.dto.notification.NotificationInfo;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/test/{userId}")
    public void test(@PathVariable Integer userId) {
        System.out.println("Sending notification to: " + userId);
        notificationService.sendToUser(new NotificationInfo(
            Map.ofEntries(),
            userId,
            NotificationType.TEST
        ));
    }
}
