package com.codemeet.controller;

import com.codemeet.service.MeetingService;
import com.codemeet.utils.dto.meeting.InstantMeetingRequest;
import com.codemeet.utils.dto.meeting.MeetingInfoResponse;
import com.codemeet.utils.dto.meeting.ScheduleMeetingRequest;
import com.codemeet.utils.dto.participant.ParticipantInfoResponse;
import com.codemeet.utils.dto.participant.ParticipantRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/meeting")
public class MeetingController {

    private final MeetingService meetingService;
    
    @GetMapping("/{meetingId}")
    public ResponseEntity<MeetingInfoResponse> getMeetingById(
        @PathVariable UUID meetingId
    ) {
        return ResponseEntity.ok(meetingService.getMeetingById(meetingId));
    }
    
    @GetMapping("/scheduled/{userId}")
    public ResponseEntity<List<MeetingInfoResponse>> getScheduledMeetingsOfUser(
        @PathVariable Integer userId
    ) {
        return ResponseEntity.ok(meetingService.getAllScheduledMeetings(userId));
    }

    @GetMapping("/previous/{userId}")
    public ResponseEntity<List<MeetingInfoResponse>> getPreviousMeetingsOfUser(
        @PathVariable Integer userId
    ) {
        return ResponseEntity.ok(meetingService.getAllPreviousMeetings(userId));
    }
    
    @GetMapping("/{meetingId}/user/{userId}")
    public ResponseEntity<ParticipantInfoResponse> getParticipantOfMeeting(
        @PathVariable UUID meetingId,
        @PathVariable Integer userId
    ) {
        return ResponseEntity.ok(meetingService.getParticipantByUserIdAndMeetingId(userId, meetingId));
    }

    @GetMapping("/{meetingId}/participants")
    public ResponseEntity<List<ParticipantInfoResponse>> getAllParticipantsOfMeeting(
        @PathVariable UUID meetingId
    ) {
        return ResponseEntity.ok(meetingService.getAllParticipantsOfMeeting(meetingId));
    }
    
    @GetMapping("/participant/{participantId}")
    public ResponseEntity<ParticipantInfoResponse> getParticipantById(
        @PathVariable UUID participantId
    ) {
        return ResponseEntity.ok(meetingService.getParticipantById(participantId));
    }
    
    @PostMapping("/participants")
    public ResponseEntity<List<ParticipantInfoResponse>> getAllParticipants(
        @RequestBody List<UUID> participantsIds
    ) {
        return ResponseEntity.ok(meetingService.getAllParticipantsByIds(participantsIds));
    }

    @PostMapping("/schedule")
    public ResponseEntity<MeetingInfoResponse> scheduleMeeting(
        @RequestBody ScheduleMeetingRequest meeting
    ) {
        return ResponseEntity.ok(meetingService.scheduleMeeting(meeting));
    }

    @PostMapping("/instant")
    public ResponseEntity<MeetingInfoResponse> instantMeeting(
        @Valid @RequestBody InstantMeetingRequest meeting
    ) {
        return ResponseEntity.ok(meetingService.startInstantMeeting(meeting));
    }
    
    @PostMapping("/request-join")
    public ResponseEntity<Void> requestJoin(
        @Valid @RequestBody ParticipantRequest participantRequest
    ) {
        meetingService.requestJoin(participantRequest);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/accept-join")
    public ResponseEntity<Void> acceptJoin(
        @Valid @RequestBody ParticipantRequest participantRequest
    ) {
        meetingService.acceptJoin(participantRequest);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/finish")
    public ResponseEntity<Void> finishMeeting(
        @Valid @RequestBody ParticipantRequest participantRequest
    ) {
        meetingService.finishMeeting(participantRequest);
        return ResponseEntity.noContent().build();
    }
}
