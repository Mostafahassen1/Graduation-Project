package com.codemeet.utils.dto.friendship;

import com.codemeet.entity.Friendship;
import com.codemeet.entity.FriendshipStatus;
import com.codemeet.utils.dto.user.UserInfoResponse;
import jakarta.validation.constraints.NotNull;

public record FriendshipInfoResponse(
    @NotNull
    Integer friendshipId,
    
    @NotNull
    UserInfoResponse other,
    
    @NotNull
    boolean isSent,

    @NotNull
    FriendshipStatus status
) {

    public static FriendshipInfoResponse of(Friendship f, Integer userId) {
        if (userId == null) userId = f.getFrom().getId();
        return new FriendshipInfoResponse(
            f.getId(),
            UserInfoResponse.of(f.getFrom().getId().equals(userId) ?
                f.getTo() : f.getFrom()),
            f.getFrom().getId().equals(userId),
            f.getStatus()
        );
    }
}
