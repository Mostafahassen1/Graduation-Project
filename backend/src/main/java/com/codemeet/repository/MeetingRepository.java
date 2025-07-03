package com.codemeet.repository;

import com.codemeet.entity.Meeting;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


public interface MeetingRepository extends JpaRepository<Meeting, UUID> {

    @Query(
        """
        SELECT m
        FROM Meeting m
        JOIN Participant p
        ON p.meeting = m
        WHERE p.user.id = :userId
        AND m.status = "FINISHED"
        """
    )
    List<Meeting> getAllPrevious(Integer userId);



    @Query(
        """
        SELECT m
        FROM Meeting m
        JOIN Participant p
        ON p.meeting = m
        WHERE p.user.id = :userId
        and m.status = "SCHEDULED"
        """
    )
    List<Meeting> getAllScheduled(Integer userId);


    @Query(
        """
        SELECT m
        FROM Meeting m
        WHERE m.startsAt BETWEEN :startTimeMinusOneSec AND :startTimePlusOneSec
        """
    )
    List<Meeting> findByStartTimeRange(LocalDateTime startTimeMinusOneSec, LocalDateTime startTimePlusOneSec);
}
