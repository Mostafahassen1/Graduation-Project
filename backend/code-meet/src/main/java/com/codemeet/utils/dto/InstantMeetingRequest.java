package com.codemeet.utils.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record InstantMeetingRequest(
    @NotNull
    @NotBlank
    @Length(max = 50)
    String title,

    @Length(max = 255)
    String description,

    @NotNull
    Integer creatorId
) {
}