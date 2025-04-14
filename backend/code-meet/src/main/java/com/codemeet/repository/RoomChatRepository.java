package com.codemeet.repository;

import com.codemeet.entity.RoomChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RoomChatRepository extends JpaRepository<RoomChat, Integer> {
    
    @Query(
        """
        SELECT rc
        FROM RoomChat rc
        WHERE rc.room.id = :roomId
        """
    )
    Optional<RoomChat> findByRoomId(Integer roomId);
    
    @Query(
        """
        SELECT rc
        FROM RoomChat rc
        WHERE rc.room.id IN (
            SELECT m.room.id
            FROM Membership m
            WHERE m.user.id = :userId
        )
        """
    )
    List<RoomChat> findAllByUserId(Integer userId);
    
    @Query(
        """
        SELECT :chatId IN(
            SELECT rc.id
            FROM RoomChat rc
            WHERE rc.room.id IN (
                SELECT ms.room.id
                FROM Membership ms
                WHERE ms.user.id = :userId
            )
        )
        """
    )
    boolean isMember(Integer userId, Integer chatId);
}
