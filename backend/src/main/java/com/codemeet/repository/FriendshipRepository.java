package com.codemeet.repository;

import com.codemeet.entity.Friendship;
import com.codemeet.utils.dto.friendship.FriendshipInfoResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.List;

public interface FriendshipRepository extends JpaRepository<Friendship, Integer> {
    @Query(
        """
        SELECT
            f.id,
            CASE
                WHEN f.from.id=:userId THEN f.to.firstName
                ELSE f.from.firstName
            END,
            CASE
                 WHEN f.from.id=:userId THEN f.to.lastName
                ELSE f.from.lastName
            END,
            CASE
                WHEN f.from.id = :userId THEN f.to.username
                ELSE f.from.username
            END,
            CASE
                WHEN f.from.id = :userId THEN f.to.profilePictureUrl
                ELSE f.from.profilePictureUrl
            END,
            f.status
        FROM Friendship f
        WHERE (f.from.id = :userId OR f.to.id = :userId)
        AND f.status = "ACCEPTED"
        """
    )
    List<FriendshipInfoResponse> getAllFriends(Integer userId);
    
    @Query(
        """
        SELECT fs
        FROM Friendship fs
        WHERE (fs.from.id = :fromId AND fs.to.id = :toId)
        OR (fs.from.id = :toId AND fs.to.id = :fromId)
        """
    )
    Optional<Friendship> findByFromIdAndToId(Integer fromId, Integer toId);
    
    @Query(
        """
        SELECT fs
        FROM Friendship fs
        WHERE fs.from.id = :userId OR fs.to.id = :userId
        ORDER BY fs.status
        """
    )
    List<Friendship> findAllByUserId(Integer userId);
    
    @Query(
        """
        SELECT fs
        FROM Friendship fs
        WHERE (fs.from.id = :userId OR fs.to.id = :userId)
        AND fs.status = "ACCEPTED"
        """
    )
    List<Friendship> findAllAcceptedByUserId(Integer userId);
    
    // Finds all friendship requests `sent by` user whose id is `userId`
    @Query(
        """
        SELECT fs
        FROM Friendship fs
        WHERE fs.from.id = :userId
        AND fs.status = "PENDING"
        """
    )
    List<Friendship> findAllPendingSentByUserId(Integer userId);
    
    // Finds all friendship requests `sent to` user whose id is `userId`
    @Query(
        """
        SELECT fs
        FROM Friendship fs
        WHERE fs.to.id = :userId
        AND fs.status = "PENDING"
        """
    )
    List<Friendship> findAllPendingReceivedByUserId(Integer userId);
    
    @Query(
        """
        SELECT COUNT(fs) > 0
        FROM Friendship fs
        WHERE (fs.from.id = :fromId AND fs.to.id = :toId)
        OR (fs.from.id = :toId AND fs.to.id = :fromId)
        """
    )
    boolean exists(Integer fromId, Integer toId);
}
