package com.codemeet.utils.dto.participant;

import com.codemeet.entity.Participant;
import com.codemeet.utils.dto.meeting.MeetingInfoResponse;
import com.codemeet.utils.dto.user.UserInfoResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ParticipantInfoResponse(
    @NotNull
    Integer participantId,

    @NotNull
    UserInfoResponse userInfo,

    MeetingInfoResponse meetingInfo
) {

    public static ParticipantInfoResponse of(Participant participant) {
        return new ParticipantInfoResponse(
            participant.getId(),
            UserInfoResponse.of(participant.getUser()),
            MeetingInfoResponse.of(participant.getMeeting())
        );
    }
}
