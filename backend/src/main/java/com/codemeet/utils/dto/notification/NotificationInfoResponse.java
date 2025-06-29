package com.codemeet.utils.dto.notification;

import com.codemeet.entity.Notification;
import com.codemeet.entity.NotificationType;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.Map;

public record NotificationInfoResponse(
    @NotNull
    Integer id,
    
    @NotNull
    Map<String, String> info,
    
    @NotNull
    Integer receiverId,
    
    @NotNull
    NotificationType type,
    
    @NotNull
    Instant sentAt
) {
    public static NotificationInfoResponse of(Notification notification) {
        return new NotificationInfoResponse(
            notification.getId(),
            notification.getInfo(),
            notification.getReceiver().getId(),
            notification.getType(),
            notification.getSentAt()
        );
    }
}
