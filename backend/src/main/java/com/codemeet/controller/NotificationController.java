package com.codemeet.controller;

import com.codemeet.entity.NotificationType;
import com.codemeet.service.NotificationService;
import com.codemeet.utils.dto.notification.NotificationInfoResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    private final NotificationService notificationService;
}
