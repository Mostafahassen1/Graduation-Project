package com.codemeet.service;

import com.codemeet.utils.exception.IllegalActionException;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class JobSchedulerService {

    private final ScheduledExecutorService scheduledExecutorService= Executors.newSingleThreadScheduledExecutor();
    private final MeetingService meetingService;
         public JobSchedulerService(@Lazy MeetingService meetingService){
             this.meetingService=meetingService;
         }
    public void scheduleMeeting(LocalDateTime meetingStartTime){
        int delay=  (meetingStartTime.getMinute()-LocalDateTime.now().getMinute());

        if(delay<=0) throw new IllegalActionException("Scheduled meeting must be at the future");

        scheduledExecutorService.schedule(()->{
            meetingService.startScheduledMeeting(meetingStartTime);
        },delay, TimeUnit.MINUTES);
    }
}
