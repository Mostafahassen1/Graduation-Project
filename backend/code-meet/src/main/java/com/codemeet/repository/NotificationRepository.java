package com.codemeet.repository;

import com.codemeet.entity.Notification;
import com.codemeet.utils.dto.NotificationDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    
    @Query(
        """
        SELECT new com.codemeet.utils.dto.NotificationDTO(n.content, n.sender.username)
        FROM Notification n
        WHERE n.receiver.id = :userId
        """
    )
    List<NotificationDTO> getNotifications(Integer userId);
}
