package com.codemeet.repository;

import com.codemeet.entity.Chat;
import com.codemeet.entity.PeerChat;
import com.codemeet.entity.RoomChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Integer> {
    
    @Query(
        """
        SELECT pc
        FROM PeerChat pc
        WHERE pc.owner.id = :ownerId
        """
    )
    List<PeerChat> findAllPeerChatEntitiesByOwnerId(Integer ownerId);
    
    @Query(
        """
        SELECT pc
        FROM PeerChat pc
        WHERE pc.id = :chatId
        """
    )
    Optional<PeerChat> findPeerChatEntityById(Integer chatId);
    
    @Query(
        """
        SELECT pc
        FROM PeerChat pc
        WHERE pc.owner.id = :ownerId AND pc.peer.id = :peerId
        """
    )
    Optional<PeerChat> findPeerChatEntityByOwnerIdAndPeerId(Integer ownerId, Integer peerId);
    
    @Query(
        """
        SELECT rc
        FROM RoomChat rc
        WHERE rc.owner.id = :ownerId
        """
    )
    List<RoomChat> findAllRoomChatEntitiesByOwnerId(Integer ownerId);
    
    @Query(
        """
        SELECT rc
        FROM RoomChat rc
        WHERE rc.id = :chatId
        """
    )
    Optional<RoomChat> findRoomChatEntityById(Integer chatId);
    
    @Query(
        """
        SELECT rc
        FROM RoomChat rc
        WHERE rc.owner.id = :ownerId AND rc.room.id = :roomId
        """
    )
    Optional<RoomChat> findRoomChatEntityByOwnerIdAndRoomId(Integer ownerId, Integer roomId);
    
    @Query(
        """
        SELECT rc
        FROM RoomChat rc
        WHERE rc.room.id = :roomId
        """
    )
    List<RoomChat> findAllRoomChatEntitiesByRoomId(Integer roomId);
}
