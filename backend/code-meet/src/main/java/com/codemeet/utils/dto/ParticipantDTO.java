package com.codemeet.utils.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ParticipantDTO
        (
        @NotNull
        String userName
        )
{
}
