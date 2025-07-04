package com.codemeet.repository;

import com.codemeet.entity.Membership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MembershipRepository extends JpaRepository<Membership, Integer> {
    
    @Query(
        """
        SELECT ms
        FROM Membership ms
        WHERE ms.user.id = :userId
        AND (ms.status = "ACCEPTED" OR ms.status = "ADMIN")
        """
    )
    List<Membership> findAllByUserId(int userId);
    
    @Query(
        """
        SELECT ms
        FROM Membership ms
        WHERE ms.room.id = :roomId
        """
    )
    List<Membership> findAllByRoomId(int roomId);
    
    @Query(
        """
        SELECT ms
        FROM Membership ms
        WHERE ms.user.id = :userId AND ms.room.id = :roomId
        """
    )
    Optional<Membership> findByUserIdAndRoomId(int userId, int roomId);
    
    @Query(
        """
        SELECT COUNT(ms) > 0
        FROM Membership ms
        WHERE ms.user.id = :userId AND ms.room.id = :roomId
        """
    )
    boolean exists(int userId, int roomId);
}
