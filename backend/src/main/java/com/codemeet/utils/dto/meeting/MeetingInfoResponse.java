package com.codemeet.utils.dto.meeting;

import com.codemeet.entity.Meeting;
import com.codemeet.entity.MeetingStatus;
import com.codemeet.utils.dto.user.UserInfoResponse;

import java.time.Instant;
import java.util.UUID;

public record MeetingInfoResponse(
    UUID meetingId,
    String title,
    String description,
    UserInfoResponse creator,
    Instant startsAt,
    MeetingStatus status
) {

    public static MeetingInfoResponse of(Meeting meeting) {
        return new MeetingInfoResponse(
            meeting.getId(),
            meeting.getTitle(),
            meeting.getDescription(),
            UserInfoResponse.of(meeting.getCreator()),
            meeting.getStartsAt(),
            meeting.getStatus()
        );
    }
}
