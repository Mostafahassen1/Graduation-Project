package com.codemeet.utils.dto.membership;

import com.codemeet.entity.Membership;
import com.codemeet.entity.MembershipStatus;
import com.codemeet.utils.dto.room.RoomInfoResponse;
import com.codemeet.utils.dto.user.UserInfoResponse;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record MembershipInfoResponse(
    @NotNull
    Integer membershipId,
    
    @NotNull
    UserInfoResponse member,
    
    @NotNull
    RoomInfoResponse room,
    
    @NotNull
    MembershipStatus status,
    
    @NotNull
    Instant joinedAt
) {
    
    public static MembershipInfoResponse of(Membership membership) {
        return new MembershipInfoResponse(
            membership.getId(),
            UserInfoResponse.of(membership.getUser()),
            RoomInfoResponse.of(membership.getRoom()),
            membership.getStatus(),
            membership.getJoinedAt()
        );
    }
}
