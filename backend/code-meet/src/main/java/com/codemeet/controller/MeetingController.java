package com.codemeet.controller;

import com.codemeet.service.MeetingService;
import com.codemeet.utils.dto.meeting.InstantMeetingRequest;
import com.codemeet.utils.dto.meeting.MeetingInfoResponse;
import com.codemeet.utils.dto.meeting.ScheduleMeetingRequest;
import com.codemeet.utils.dto.participant.ParticipantInfoResponse;
import com.codemeet.utils.dto.participant.ParticipantRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@AllArgsConstructor
@RestController
@RequestMapping("/api/meeting")
public class MeetingController {

    private final MeetingService meetingService;

    @PostMapping("/participant/add")
    public ResponseEntity<ParticipantInfoResponse> joinMeeting(
          @Valid  @RequestBody ParticipantRequest participantRequest
    ) {
        return ResponseEntity.ok(meetingService.joinMeeting(participantRequest));
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

    @GetMapping("/{meetingId}/participant/all")
    public ResponseEntity<List<ParticipantInfoResponse>> getAllParticipantsOfMeeting(
        @PathVariable Integer meetingId
    ) {
        return ResponseEntity.ok(meetingService.getAllParticipantsOfMeeting(meetingId));
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

    @PatchMapping("/close")
    public ResponseEntity<Void> closeMeeting(
      @Valid  @RequestBody ParticipantRequest participantRequest
    ) {
        meetingService.closeMeeting(participantRequest);
        return ResponseEntity.noContent().build();
    }




}
