package com.codemeet.utils.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record RoomUpdateRequest(
    
    @NotNull
    Integer roomId,
    
    @NotNull
    @NotBlank
    @Length(max = 50)
    String name,
    
    @NotNull
    @Length(max = 255)
    String description,
    
    String roomPictureUrl
) {
}
