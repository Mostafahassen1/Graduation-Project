package com.codemeet.utils.dto.membership;

import jakarta.validation.constraints.NotNull;

public record MembershipRequest(
    @NotNull
    Integer userId,
    
    @NotNull
    Integer roomId
) {
}
