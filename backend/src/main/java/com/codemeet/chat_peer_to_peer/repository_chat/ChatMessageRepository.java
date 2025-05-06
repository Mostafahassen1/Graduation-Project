
package com.codemeet.chat_peer_to_peer.repository_chat;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codemeet.chat_peer_to_peer.entity_chat.ChatMessage;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {


   List<ChatMessage> findAllByChatId(Long chatId);
}




