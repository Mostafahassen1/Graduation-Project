package com.codemeet.utils.dto.participant;

import com.codemeet.entity.Participant;
import com.codemeet.utils.dto.user.UserInfoResponse;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ParticipantInfoResponse(
    @NotNull
    UUID participantId,

    @NotNull
    UUID meetingId,

    @NotNull
    UserInfoResponse user
) {

    public static ParticipantInfoResponse of(Participant participant) {
        return new ParticipantInfoResponse(
            participant.getId(),
            participant.getMeeting().getId(),
            UserInfoResponse.of(participant.getUser())
        );
    }
}
