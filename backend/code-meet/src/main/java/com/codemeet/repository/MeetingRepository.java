package com.codemeet.repository;

import com.codemeet.entity.Meeting;
import com.codemeet.utils.dto.MeetingInfoResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface MeetingRepository extends JpaRepository<Meeting, Integer> {



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
}
