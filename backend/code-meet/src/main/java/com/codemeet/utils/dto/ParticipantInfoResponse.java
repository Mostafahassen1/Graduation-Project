package com.codemeet.utils.dto;

import com.codemeet.entity.Participant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ParticipantInfoResponse(
    @NotNull
    Integer participantId,

    @NotNull
    @NotBlank
    String firstName,

    @NotNull
    @NotBlank
    String lastName,

    @NotNull
    @NotBlank
    String username,

    @NotNull
    Integer meetingId,

    String profilePictureUrl
) {

    public static ParticipantInfoResponse of(Participant participant) {
        return new ParticipantInfoResponse(
            participant.getId(),
            participant.getUser().getFirstName(),
            participant.getUser().getLastName(),
            participant.getUser().getUsername(),
            participant.getMeeting().getId(),
            participant.getUser().getProfilePictureUrl()
        );
    }
}
