package com.codemeet.repository;

import com.codemeet.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {
    
    @Query(
        """
        SELECT m
        FROM Message m
        WHERE m.chat.id = :chatId
        """
    )
    List<Message> findAllByChatId(Integer chatId);
    
    @Query(
        """
        SELECT m
        FROM Message m
        WHERE m.sender.id = :senderId
        """
    )
    List<Message> findAllBySenderId(Integer senderId);
    
    @Query(
        """
        SELECT m
        FROM Message m
        WHERE m.chat.id = :chatId
        AND m.sentAt > :instant
        """
    )
    List<Message> findAllSentAfterByChatId(Integer chatId, Instant instant);
}
