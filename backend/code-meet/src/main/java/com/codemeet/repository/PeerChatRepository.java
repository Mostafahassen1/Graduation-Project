package com.codemeet.repository;


import com.codemeet.entity.PeerChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PeerChatRepository extends JpaRepository<PeerChat, Integer> {
    
    @Query(
        """
        SELECT pc
        FROM PeerChat pc
        WHERE (pc.one.id = :oneId AND pc.theOther.id = :theOtherId)
        OR (pc.one.id = :theOtherId AND pc.theOther.id = :oneId)
        """
    )
    Optional<PeerChat> findByOneAndTheOtherIds(Integer oneId, Integer theOtherId);
    
    @Query(
        """
        SELECT pc
        FROM PeerChat pc
        WHERE pc.one.id = :userId OR pc.theOther.id = :userId
        """
    )
    List<PeerChat> findAllByUserId(Integer userId);
}
