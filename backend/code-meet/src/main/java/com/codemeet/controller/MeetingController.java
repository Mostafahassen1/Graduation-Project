package com.codemeet.controller;

import com.codemeet.service.MeetingService;
import com.codemeet.utils.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/meeting")
public class MeetingController {

    private final MeetingService meetingService;

    public MeetingController(MeetingService meetingService) {
        this.meetingService = meetingService;
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
        @RequestBody InstantMeetingRequest meeting
    ) {
        return ResponseEntity.ok(meetingService.startInstantMeeting(meeting));
    }

    @PatchMapping("/close")
    public ResponseEntity<Void> closeMeeting(
        @RequestBody ParticipantRequest participantRequest
    ) {
        meetingService.closeMeeting(participantRequest);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/participant/add")
    public ResponseEntity<ParticipantInfoResponse> addParticipant(
        @RequestBody ParticipantRequest participantRequest
    ) {
        return ResponseEntity.ok(meetingService.addParticipantToMeeting(participantRequest));
    }

    @DeleteMapping("/participant/remove")
    public ResponseEntity<Void> removeParticipant(
        @RequestBody ParticipantRequest participantRequest
    ) {
        meetingService.removeParticipantFromMeeting(participantRequest);
        return ResponseEntity.noContent().build();
    }
}
