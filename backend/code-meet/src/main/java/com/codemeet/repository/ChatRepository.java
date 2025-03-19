package com.codemeet.repository;

import com.codemeet.entity.Chat;
import com.codemeet.entity.RoomChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Integer> {
    
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
    List<RoomChat> findAllRoomChatsByUserId(Integer userId);
}
