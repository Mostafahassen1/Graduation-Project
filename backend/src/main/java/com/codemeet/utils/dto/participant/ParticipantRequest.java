package com.codemeet.utils.dto.participant;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ParticipantRequest(
    @NotNull
    Integer userId,

    @NotNull
    UUID meetingId
) {
}
