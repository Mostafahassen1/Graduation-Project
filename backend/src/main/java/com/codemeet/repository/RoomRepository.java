package com.codemeet.repository;

import com.codemeet.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Integer> {
    
    @Query(
        """
        SELECT r
        FROM Room r
        WHERE r.creator.id = :creatorId
        """
    )
    List<Room> findAllByCreatorId(Integer creatorId);
}
