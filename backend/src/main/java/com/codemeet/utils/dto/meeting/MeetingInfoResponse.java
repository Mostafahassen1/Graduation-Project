package com.codemeet.utils.dto.meeting;

import com.codemeet.entity.Meeting;
import com.codemeet.utils.dto.user.UserInfoResponse;

import java.time.Instant;

public record MeetingInfoResponse(
    Integer meetingId,
    String title,
    String description,
    UserInfoResponse creatorInfo,
    Instant startsAt
) {

    public static MeetingInfoResponse of(Meeting meeting) {
        return new MeetingInfoResponse(
            meeting.getId(),
            meeting.getTitle(),
            meeting.getDescription(),
            UserInfoResponse.of(meeting.getCreator()),
            meeting.getStartsAt()
        );
    }
}
