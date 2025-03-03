package com.codemeet.repository;

import com.codemeet.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant, Integer> {

    @Query(
        """
        SELECT p
        FROM Participant p
        WHERE p.user.username = :username AND p.meeting.id = :meetingId
        """
    )
    Optional<Participant> findByUsernameAndMeetingId(String username, Integer meetingId);

    @Query(
        """
        SELECT p
        FROM Participant p
        WHERE p.meeting.id = :meetingId
        """
    )
    List<Participant> findByMeetingId(Integer meetingId);

    @Modifying
    @Query(
        """
        DELETE FROM Participant p
        WHERE p.user.username = :username and p.meeting.id = :meetingId
        """
    )
    void deleteByUsernameAndMeetingId(String username, Integer meetingId);

    @Query(
        """
        SELECT EXISTS (
            SELECT p
            FROM Participant p
            WHERE p.user.username = :username and p.meeting.id = :meetingId
        )
        """
    )
    boolean existsByUsernameAndMeetingId(String username, Integer meetingId);
}