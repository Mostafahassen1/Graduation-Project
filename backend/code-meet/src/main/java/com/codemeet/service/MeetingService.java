package com.codemeet.service;

import com.codemeet.entity.Meeting;
import com.codemeet.entity.MeetingStatus;
import com.codemeet.entity.Participant;
import com.codemeet.entity.User;
import com.codemeet.repository.MeetingRepository;
import com.codemeet.repository.ParticipantRepository;
import com.codemeet.utils.dto.*;
import com.codemeet.utils.exception.EntityNotFoundException;
import com.codemeet.utils.exception.IllegalActionException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final ParticipantRepository participantRepository;
    private final NotificationService notificationService;
    private final UserService userService;

    public MeetingService(
            MeetingRepository meetingRepository,
            ParticipantRepository participantRepository,
            NotificationService notificationService,
            UserService userService
    ) {
        this.meetingRepository = meetingRepository;
        this.notificationService = notificationService;
        this.participantRepository = participantRepository;
        this.userService = userService;
    }

    public Meeting getMeetingEntityById(Integer meetingId) {
        return meetingRepository.findById(meetingId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Meeting with id '%d' not found".formatted(meetingId)));
    }

    public List<Meeting> getAllPreviousMeetingEntities(Integer userId) {
        userService.getUserEntityById(userId);
        return meetingRepository.getAllPrevious(userId);
    }

    public List<Meeting> getAllScheduledMeetingEntities(Integer userId) {
        userService.getUserEntityById(userId);
        return meetingRepository.getAllScheduled(userId);
    }

    public Participant getParticipantEntityById(Integer participantId) {
        return participantRepository.findById(participantId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Participant with id '%d' not found".formatted(participantId)));
    }

    public Participant getParticipantEntityByUsernameAndMeetingId(
            String username, Integer meetingId
    ) {
        return participantRepository.findByUsernameAndMeetingId(username, meetingId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Participant with username '%s' and meetingId '%d' not found"
                                .formatted(username, meetingId)));
    }

    public List<Participant> getAllParticipantEntitiesByMeetingId(Integer meetingId) {
        getMeetingEntityById(meetingId); // Ensures that this meeting exists
        return participantRepository.findByMeetingId(meetingId);
    }

    public List<MeetingInfoResponse> getAllPreviousMeetings(Integer userId) {
        return getAllPreviousMeetingEntities(userId).stream()
                .map(MeetingInfoResponse::of)
                .toList();
    }

    public List<MeetingInfoResponse> getAllScheduledMeetings(Integer userId) {
        return getAllScheduledMeetingEntities(userId).stream()
                .map(MeetingInfoResponse::of)
                .toList();
    }

    public ParticipantInfoResponse getParticipantById(Integer participantId) {
        return ParticipantInfoResponse.of(getParticipantEntityById(participantId));
    }

    public ParticipantInfoResponse getParticipantByUsernameAndMeetingId(
            String username, Integer meetingId
    ) {
        return ParticipantInfoResponse.of(
                getParticipantEntityByUsernameAndMeetingId(username, meetingId));
    }

    public List<ParticipantInfoResponse> getAllParticipantsOfMeeting(Integer meetingId) {
        return getAllParticipantEntitiesByMeetingId(meetingId).stream()
                .map(ParticipantInfoResponse::of)
                .toList();
    }

    @Transactional
    public MeetingInfoResponse scheduleMeeting(
            ScheduleMeetingRequest scheduledMeetingRequest
    ) {
        // Schedule meeting
        User creator = userService.getUserEntityById(scheduledMeetingRequest.creatorId());

        Meeting scheduledMeeting = new Meeting(
                scheduledMeetingRequest.title(),
                scheduledMeetingRequest.description(),
                creator,
                scheduledMeetingRequest.startsAt(),
                MeetingStatus.SCHEDULED
        );

        meetingRepository.save(scheduledMeeting);

        // Add participants and creator to list of participants
        List<Participant> participants = new ArrayList<>(
                scheduledMeetingRequest.participants().stream()
                        .map(username -> new Participant(
                                userService.getUserEntityByUsername(username),
                                scheduledMeeting
                        )).toList()
        );

        participants.add(new Participant(creator, scheduledMeeting));

        participantRepository.saveAll(participants);

        //TODO: Send notifications to participants
        //TODO: Schedule job to run at given time which notifies creator client to start the meeting now
        //TODO: and notifies all participants that there is a meeting now

        return MeetingInfoResponse.of(scheduledMeeting);
    }

    @Transactional
    public MeetingInfoResponse startInstantMeeting(
            InstantMeetingRequest instantMeetingRequest
    ) {
        // Create instant meeting
        User creator = userService.getUserEntityById(instantMeetingRequest.creatorId());

        Meeting instantMeeting = new Meeting(
                instantMeetingRequest.title(),
                instantMeetingRequest.description(),
                creator,
                Instant.now(),
                MeetingStatus.RUNNING
        );

        meetingRepository.save(instantMeeting);
        participantRepository.save(new Participant(creator, instantMeeting));

        //TODO: Run job to notify creator client to start the meeting now

        return MeetingInfoResponse.of(instantMeeting);
    }

    @Transactional
    public ParticipantInfoResponse addParticipantToMeeting(ParticipantRequest participantRequest) {
        //TODO: Possible to check cache before hitting DB
        User user = userService.getUserEntityByUsername(participantRequest.username());
        Meeting meeting = getMeetingEntityById(participantRequest.meetingId());

        //TODO: Some cases to handle:
        //  - A user joins then leaves then joins again.
        //       - Avoid reinserting that user to the same meeting.
        //  - Adding a user to non running meeting.

        Participant participant = participantRepository.save(
                new Participant(user, meeting)
        );

        return ParticipantInfoResponse.of(participant);
    }

    @Transactional
    public void removeParticipantFromMeeting(ParticipantRequest participantRequest) {
        //TODO: Possible to check cache before hitting DB
        Participant participant = getParticipantEntityByUsernameAndMeetingId(
                participantRequest.username(), participantRequest.meetingId()
        );

        if (isMeetingCreator(participant)) {
            throw new IllegalActionException(
                    "Meeting creator can't be removed from the meeting");
        }

        participantRepository.delete(participant);
    }

    @Transactional
    public void removeParticipantFromMeeting(Integer participantId) {
        Participant participant = getParticipantEntityById(participantId);

        if (isMeetingCreator(participant)) {
            throw new IllegalActionException(
                    "Meeting creator can't be removed from the meeting");
        }

        participantRepository.delete(participant);
    }

    public void triggerMeeting() {
        //TODO: This function sends notifying logic for creator and other participants and called by scheduled job at specific time
    }

    /**
     * Closes a running meeting.
     * @param participantRequest a request for closing the meeting by a user.
     */
    @Transactional
    public void closeMeeting(ParticipantRequest participantRequest) {
        Participant participant = getParticipantEntityByUsernameAndMeetingId(
                participantRequest.username(), participantRequest.meetingId()
        );

        //TODO: Some cases to handle:
        //  - The meeting creator is the only one who can close the meeting.
        //  - The meeting should be running to close it.

        participant.getMeeting().setStatus(MeetingStatus.FINISHED);
    }

    private boolean isMeetingCreator(Participant p) {
        return p.getMeeting().getCreator().getId().equals(p.getUser().getId());
    }
}
