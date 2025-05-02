package com.codemeet.service;

import com.codemeet.entity.Meeting;
import com.codemeet.entity.Participant;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class JobSchedulerService {

    private final ScheduledExecutorService scheduledExecutorService =
        Executors.newSingleThreadScheduledExecutor();
    private final MeetingService meetingService;

    public JobSchedulerService(@Lazy MeetingService meetingService) {
        this.meetingService = meetingService;
    }
    
    public void scheduleMeeting(Meeting meeting) {
        long delay = Duration.between(Instant.now(), meeting.getStartsAt()).toMillis();
        
        System.out.println("Meeting starts after " + delay / 1000 + " seconds...");
        
        this.scheduledExecutorService.schedule(() -> {
            meetingService.startScheduledMeeting(meeting);
        }, delay, TimeUnit.MILLISECONDS);
    }
}
