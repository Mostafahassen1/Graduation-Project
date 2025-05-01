package com.codemeet.service;

import com.codemeet.entity.*;
import com.codemeet.repository.MeetingRepository;
import com.codemeet.repository.ParticipantRepository;
import com.codemeet.utils.dto.meeting.InstantMeetingRequest;
import com.codemeet.utils.dto.meeting.MeetingInfoResponse;
import com.codemeet.utils.dto.meeting.ScheduleMeetingRequest;
import com.codemeet.utils.dto.notification.NotificationInfo;
import com.codemeet.utils.dto.participant.ParticipantInfoResponse;
import com.codemeet.utils.dto.participant.ParticipantRequest;
import com.codemeet.utils.exception.EntityNotFoundException;
import com.codemeet.utils.exception.IllegalActionException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.swing.text.html.Option;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static com.codemeet.entity.NotificationType.SCHEDULED_MEETING;

@Service
@AllArgsConstructor
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final ParticipantRepository participantRepository;
    private final NotificationService notificationService;
    private final UserService userService;
    private final JobSchedulerService jobSchedulerService;



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

        Meeting scheduledMeeting =
                Meeting.builder()
                        .title(scheduledMeetingRequest.title())
                        .description(scheduledMeetingRequest.description())
                        .creator(creator)
                        .startsAt(scheduledMeetingRequest.startsAt())
                        .status(MeetingStatus.SCHEDULED)
                        .isInstant(false)
                        .build();


        meetingRepository.save(scheduledMeeting);

        // Add participants and creator to a list of participants
        List<Participant> participants = new ArrayList<>(
            scheduledMeetingRequest.participants().stream()
                .map(username -> Participant.builder().meeting(scheduledMeeting)
                        .user(  userService.getUserEntityByUsername(username))
                        .isParticipated(false).build())
                         .toList()
        );

        participants.add(Participant.builder()
                .meeting(scheduledMeeting)
                .user(creator)
                .isParticipated(false)
                .build());

        List<Participant>savedParticipants=participantRepository.saveAll(participants);

        // Send notifications to all participants...
        TransactionSynchronizationManager.registerSynchronization(
            new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                   notifyMeetingParticipants(savedParticipants,scheduledMeeting);
                    jobSchedulerService.scheduleMeeting(scheduledMeetingRequest.startsAt());
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

        Meeting instantMeeting =
                Meeting.builder()
                        .title(instantMeetingRequest.title())
                        .description(instantMeetingRequest.description())
                        .creator(creator)
                        .startsAt(LocalDateTime.now())
                        .status(MeetingStatus.RUNNING)
                        .isInstant(true)
                        .build();


        meetingRepository.save(instantMeeting);
        participantRepository.save(
                Participant.builder()
                        .meeting(instantMeeting)
                        .user(creator)
                        .isParticipated(true)
                        .build()
        );

        return MeetingInfoResponse.of(instantMeeting);
    }

    @Transactional
    public ParticipantInfoResponse joinMeeting(ParticipantRequest participantRequest) {


        Meeting meeting = getMeetingEntityById(participantRequest.meetingId());
        Optional<Participant> meetingParticipant = participantRepository.findByUsernameAndMeetingId(
                participantRequest.username(),
                participantRequest.meetingId()
        );

        Participant participant;

        if (meetingParticipant.isPresent()) {
            participant = meetingParticipant.get();
            participant.setParticipated(true);
            participantRepository.save(participant); // Optional: persist updated flag
        } else {
            if (meeting.isInstant()) {
                User user = userService.getUserEntityByUsername(participantRequest.username());
                participant = participantRepository.save(
                        Participant.builder()
                                .meeting(meeting)
                                .user(user)
                                .isParticipated(true)
                                .build()
                );
            } else {
                throw new IllegalActionException("You have no access for that meeting");
            }
        }

        return ParticipantInfoResponse.of(participant);
    }



    @Transactional
   void startScheduledMeeting(LocalDateTime meetingStart){
        LocalDateTime startTimeMinusOneSec = meetingStart.minusSeconds(1);
        LocalDateTime startTimePlusOneSec = meetingStart.plusSeconds(1);
        List<Meeting> meetings=meetingRepository.findByStartTimeRange(startTimeMinusOneSec,startTimePlusOneSec);
        meetings.forEach(meeting -> {
            List<Participant> participants=participantRepository.findByMeetingId(meeting.getId());
            notifyMeetingParticipants(participants,meeting);

            meeting.setStatus(MeetingStatus.RUNNING);
        });

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


    private void notifyMeetingParticipants(List<Participant> participants,Meeting scheduledMeeting){
        for (Participant participant : participants) {
            Map<String, Object> info = new LinkedHashMap<>();
            info.put("creatorUsername", scheduledMeeting.getCreator().getUsername());
            info.put("creatorFullName", scheduledMeeting.getCreator().getFullName());
            info.put("meetingTitle", scheduledMeeting.getTitle());
           // info.put("startsAt", scheduledMeeting.getStartsAt());
            info.put("meetingId",scheduledMeeting.getId());

            notificationService.sendToUser(new NotificationInfo(
                    info, participant.getUser().getId(), SCHEDULED_MEETING
            ));

        }
    }


}
