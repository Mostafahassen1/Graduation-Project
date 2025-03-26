package com.codemeet.utils.dto;

import com.codemeet.entity.NotificationType;

import java.util.Map;

public record NotificationInfo(
    Map<String, Object> info,
    
    Integer receiverId,
    
    NotificationType type
) {
}
