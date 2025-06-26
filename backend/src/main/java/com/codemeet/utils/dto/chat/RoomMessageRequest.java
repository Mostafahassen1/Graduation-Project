package com.codemeet.utils.dto.chat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RoomMessageRequest(
    @NotNull
    Integer ownerId, // Sender
    
    @NotNull
    Integer roomId,
    
    @NotNull
    @NotBlank
    String content
) {
}