package com.codemeet.utils.dto.membership;

import com.codemeet.entity.Membership;
import com.codemeet.entity.MembershipStatus;
import jakarta.validation.constraints.NotNull;

public record MembershipInfoResponse(
    @NotNull
    Integer membershipId,
    
    @NotNull
    MembershipStatus status
) {
    
    public static MembershipInfoResponse of(Membership membership) {
        return new MembershipInfoResponse(
            membership.getId(),
            membership.getStatus()
        );
    }
}
