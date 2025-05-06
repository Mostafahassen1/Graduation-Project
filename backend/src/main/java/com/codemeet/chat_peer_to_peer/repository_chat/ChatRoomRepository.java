
package com.codemeet.chat_peer_to_peer.repository_chat;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codemeet.chat_peer_to_peer.entity_chat.ChatRoom;
import com.codemeet.entity.User;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    Optional<ChatRoom> findBySenderAndReceiver(User sender, User receiver);

    Optional<ChatRoom> findByReceiverAndSender(User sender, User receiver);
}




