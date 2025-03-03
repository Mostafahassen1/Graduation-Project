package com.codemeet.utils.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record RoomCreationRequest(
    @NotNull
    @NotBlank
    String name,
    
    @NotNull
    @Length(max = 255)
    String description,
    
    @NotNull
    Integer creatorId,
    
    String roomPictureUrl
) {
}
