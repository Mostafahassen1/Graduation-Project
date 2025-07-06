package com.codemeet.repository;

import com.codemeet.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ParticipantRepository extends JpaRepository<Participant, UUID> {

    @Query(
        """
        SELECT p
        FROM Participant p
        WHERE p.user.username = :username AND p.meeting.id = :meetingId
        """
    )
    Optional<Participant> findByUsernameAndMeetingId(String username, UUID meetingId);
    
    @Query(
        """
        SELECT p
        FROM Participant p
        WHERE p.user.id = :userId AND p.meeting.id = :meetingId
        """
    )
    Optional<Participant> findByUserIdAndMeetingId(Integer userId, UUID meetingId);

    @Query(
        """
        SELECT p
        FROM Participant p
        WHERE p.meeting.id = :meetingId
        """
    )
    List<Participant> findAllByMeetingId(UUID meetingId);

    @Query(
        """
        SELECT EXISTS (
            SELECT p
            FROM Participant p
            WHERE p.user.username = :username and p.meeting.id = :meetingId
        )
        """
    )
    boolean existsByUsernameAndMeetingId(String username, UUID meetingId);

    @Query(
        """
        SELECT EXISTS (
            SELECT p
            FROM Participant p
            WHERE p.user.id = :userId and p.meeting.id = :meetingId
        )
        """
    )
    boolean existsByUserIdAndMeetingId(Integer userId, UUID meetingId);
}