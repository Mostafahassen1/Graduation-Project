package com.codemeet.utils.dto.chat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PeerMessageRequest(
    @NotNull
    Integer ownerId, // Sender
    
    @NotNull
    Integer peerId,
    
    @NotNull
    @NotBlank
    String content
) {
}
