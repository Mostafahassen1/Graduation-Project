package com.codemeet.utils.dto.friendship;

import jakarta.validation.constraints.NotNull;

public record FriendshipRequest(
    @NotNull
    Integer fromId,

    @NotNull
    Integer toId
) {
}
