package com.codemeet.service;

import com.codemeet.entity.*;
import com.codemeet.repository.MeetingRepository;
import com.codemeet.repository.ParticipantRepository;
import com.codemeet.utils.dto.meeting.InstantMeetingRequest;
import com.codemeet.utils.dto.meeting.MeetingInfoResponse;
import com.codemeet.utils.dto.meeting.ScheduleMeetingRequest;
import com.codemeet.utils.dto.participant.ParticipantInfoResponse;
import com.codemeet.utils.dto.participant.ParticipantRequest;
import com.codemeet.utils.exception.EntityNotFoundException;
import com.codemeet.utils.exception.IllegalActionException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.Instant;
import java.util.*;

import static com.codemeet.entity.MeetingStatus.*;

@Service
@RequiredArgsConstructor
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final ParticipantRepository participantRepository;
    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;
    private final UserService userService;
    private final JobSchedulerService jobSchedulerService;

    public Meeting getMeetingEntityById(UUID meetingId) {
        return meetingRepository.findById(meetingId)
            .orElseThrow(() -> new EntityNotFoundException(
                "Meeting with id '%s' not found".formatted(meetingId)));
    }

    public List<Meeting> getAllPreviousMeetingEntities(Integer userId) {
        userService.getUserEntityById(userId);
        return meetingRepository.getAllPrevious(userId);
    }

    public List<Meeting> getAllScheduledMeetingEntities(Integer userId) {
        userService.getUserEntityById(userId);
        return meetingRepository.getAllScheduled(userId);
    }
    
    public Participant getParticipantEntityById(UUID participantId) {
        return participantRepository.findById(participantId)
            .orElseThrow(() -> new EntityNotFoundException(
                "Participant with id '%s' not found"
                    .formatted(participantId)));
    }
    
    public List<Participant> getAllParticipantEntitiesByIds(List<UUID> participantsIds) {
        return this.participantRepository.findAllById(participantsIds);
    }

    public Participant getParticipantEntityByUsernameAndMeetingId(
            String username, UUID meetingId
    ) {
        return participantRepository.findByUsernameAndMeetingId(username, meetingId)
            .orElseThrow(() -> new EntityNotFoundException(
                "Participant with username '%s' and meetingId '%s' not found"
                    .formatted(username, meetingId)));
    }
    
    public Participant getParticipantEntityByUserIdAndMeetingId(
        Integer userId, UUID meetingId
    ) {
        return participantRepository.findByUserIdAndMeetingId(userId, meetingId)
            .orElseThrow(() -> new EntityNotFoundException(
                "Participant with userId '%d' and meetingId '%s' not found"
                    .formatted(userId, meetingId)));
    }

    public List<Participant> getAllParticipantEntitiesByMeetingId(UUID meetingId) {
        getMeetingEntityById(meetingId); // Ensures that this meeting exists
        return participantRepository.findAllByMeetingId(meetingId);
    }
    
    public MeetingInfoResponse getMeetingById(UUID meetingId) {
        return MeetingInfoResponse.of(getMeetingEntityById(meetingId));
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
    
    public ParticipantInfoResponse getParticipantById(UUID participantId) {
        return ParticipantInfoResponse.of(getParticipantEntityById(participantId));
    }
    
    public List<ParticipantInfoResponse> getAllParticipantsByIds(List<UUID> participantsIds) {
        return getAllParticipantEntitiesByIds(participantsIds).stream()
            .map(ParticipantInfoResponse::of)
            .toList();
    }
    
    public ParticipantInfoResponse getParticipantByUsernameAndMeetingId(
        String username, UUID meetingId
    ) {
        return ParticipantInfoResponse.of(
            getParticipantEntityByUsernameAndMeetingId(username, meetingId));
    }
    
    public ParticipantInfoResponse getParticipantByUserIdAndMeetingId(
        Integer userId, UUID meetingId
    ) {
        return ParticipantInfoResponse.of(
            getParticipantEntityByUserIdAndMeetingId(userId, meetingId));
    }

    public List<ParticipantInfoResponse> getAllParticipantsOfMeeting(UUID meetingId) {
        return getAllParticipantEntitiesByMeetingId(meetingId).stream()
            .map(ParticipantInfoResponse::of)
            .toList();
    }

    @Transactional
    public MeetingInfoResponse scheduleMeeting(
        ScheduleMeetingRequest scheduleMeetingRequest
    ) {
        // Schedule meeting
        User creator = userService.getUserEntityById(scheduleMeetingRequest.creatorId());

        Meeting scheduledMeeting = Meeting.builder()
            .title(scheduleMeetingRequest.title())
            .description(scheduleMeetingRequest.description())
            .creator(creator)
            .startsAt(scheduleMeetingRequest.startsAt())
            .status(SCHEDULED)
            .isInstant(false)
            .build();

        meetingRepository.save(scheduledMeeting);

        // Add participants and creator to a list of participants
        List<Participant> participants = new ArrayList<>(
            scheduleMeetingRequest.participants().stream()
                .map(username -> Participant.builder()
                    .meeting(scheduledMeeting)
                    .user(userService.getUserEntityByUsername(username))
                    .isParticipated(false)
                    .build()
                )
                .toList()
        );

        participants.add(Participant.builder()
            .meeting(scheduledMeeting)
            .user(creator)
            .isParticipated(false)
            .build());

        List<Participant> savedParticipants = participantRepository.saveAll(participants);

        // Send notifications to all participants...
        TransactionSynchronizationManager.registerSynchronization(
            new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    notificationService.sendMeetingScheduledNotification(scheduledMeeting, participants);
                    jobSchedulerService.scheduleMeeting(scheduledMeeting);
                }
            }
        );

        return MeetingInfoResponse.of(scheduledMeeting);
    }

    @Transactional
    public MeetingInfoResponse startInstantMeeting(
        InstantMeetingRequest instantMeetingRequest
    ) {
        User creator = userService.getUserEntityById(instantMeetingRequest.creatorId());

        Meeting instantMeeting = Meeting.builder()
            .title(instantMeetingRequest.title())
            .description(instantMeetingRequest.description())
            .creator(creator)
            .startsAt(Instant.now())
            .status(RUNNING)
            .isInstant(true)
            .build();

        meetingRepository.save(instantMeeting);
        participantRepository.save(Participant.builder()
            .meeting(instantMeeting)
            .user(creator)
            .isParticipated(true)
            .build()
        );

        return MeetingInfoResponse.of(instantMeeting);
    }

    public void requestJoin(ParticipantRequest participantRequest) {
        if (!participantRepository.existsByUserIdAndMeetingId(
            participantRequest.userId(),
            participantRequest.meetingId()
        )) {
            messagingTemplate.convertAndSend(
                "/request-join/" + participantRequest.meetingId(),
                participantRequest.userId()
            );
        }
    }
    
    @Transactional
    public void acceptJoin(ParticipantRequest participantRequest) {
        if (!participantRepository.existsByUserIdAndMeetingId(
            participantRequest.userId(),
            participantRequest.meetingId()
        )) {
            User user = userService.getUserEntityById(participantRequest.userId());
            Meeting meeting = getMeetingEntityById(participantRequest.meetingId());
            
            Participant participant = Participant.builder()
                .meeting(meeting)
                .user(user)
                .build();
            
            participantRepository.save(participant);
            
            messagingTemplate.convertAndSend(
                "/accept-join/" + participantRequest.userId() + "/" + participantRequest.meetingId(),
                ParticipantInfoResponse.of(participant)
            );
        }
    }

    @Transactional
    void startScheduledMeeting(Meeting meeting) {
        meeting.setStatus(RUNNING);
        meetingRepository.save(meeting);
        List<Participant> participants =
            getAllParticipantEntitiesByMeetingId(meeting.getId());
        
        TransactionSynchronizationManager.registerSynchronization(
            new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    notificationService.sendMeetingStartedNotification(meeting, participants);
                }
            }
        );
    }

    /**
     * Finishes a running meeting.
     * @param participantRequest a request for finishing the meeting by a user.
     */
    @Transactional
    public void finishMeeting(ParticipantRequest participantRequest) {
        Participant participant = getParticipantEntityByUserIdAndMeetingId(
                participantRequest.userId(),
                participantRequest.meetingId()
            );
        Meeting meeting = participant.getMeeting();
        
        // The meeting should be running to close it.
        if (meeting.getStatus() == RUNNING) {
            // The meeting creator is the only one who can finish the meeting.
            if (meeting.getCreator().getId().equals(participant.getUser().getId())) {
                meeting.setStatus(FINISHED);
            } else {
                throw new IllegalActionException("Only meeting creator can finish the meeting.");
            }
        } else {
            throw new IllegalActionException("Only a running meeting can be closed.");
        }
    }
}
