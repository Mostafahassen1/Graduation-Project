package com.codemeet.utils.dto;

import com.codemeet.utils.annotation.FutureTime;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.Instant;
import java.util.Set;

public record ScheduleMeetingRequest(
    @NotNull
    @NotBlank
    @Length(max = 50)
    String title,

    @Length(max = 255)
    String description,

    @NotNull
    Integer creatorId,

    @NotNull
    @FutureTime
    Instant startsAt,

    @NotEmpty
    Set<@NotNull @NotBlank String> participants
) {
}
