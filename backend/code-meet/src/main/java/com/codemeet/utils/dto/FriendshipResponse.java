package com.codemeet.utils.dto;

import com.codemeet.entity.Friendship;
import com.codemeet.entity.FriendshipStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FriendshipResponse(
    @NotNull
    Integer friendshipId,

    @NotNull
    @NotBlank
    String otherFirstName,

    @NotNull
    @NotBlank
    String otherLastName,

    @NotNull
    @NotBlank
    String otherUsername,

    String otherProfilePictureUrl,

    @NotNull
    FriendshipStatus status
) {

    public static FriendshipResponse of(Friendship f, Integer userId) {
        return f.getFrom().getId().equals(userId) ?
            new FriendshipResponse(
                f.getId(),
                f.getTo().getFirstName(),
                f.getTo().getLastName(),
                f.getTo().getUsername(),
                f.getTo().getProfilePictureUrl(),
                f.getStatus()
            ) :
            new FriendshipResponse(
                f.getId(),
                f.getFrom().getFirstName(),
                f.getFrom().getLastName(),
                f.getFrom().getUsername(),
                f.getFrom().getProfilePictureUrl(),
                f.getStatus()
            );
    }
}
