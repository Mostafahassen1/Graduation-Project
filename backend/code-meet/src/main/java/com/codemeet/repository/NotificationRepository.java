package com.codemeet.repository;

import com.codemeet.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    
    @Query(
        """
        SELECT n
        FROM Notification n
        WHERE n.receiver.id = :userId
        """
    )
    List<Notification> getNotifications(Integer userId);
}
