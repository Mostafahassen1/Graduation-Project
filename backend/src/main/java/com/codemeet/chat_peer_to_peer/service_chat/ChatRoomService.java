
package com.codemeet.chat_peer_to_peer.service_chat;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.codemeet.chat_peer_to_peer.entity_chat.ChatRoom;
import com.codemeet.chat_peer_to_peer.repository_chat.ChatRoomRepository;
import com.codemeet.entity.User;

@Service
public class ChatRoomService {


    private final ChatRoomRepository chatRoomRepository;

    public ChatRoomService(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }

    public Long getChatRoomId(User sender, User receiver ) {


        Optional<ChatRoom> existingChatRoom = chatRoomRepository.findBySenderAndReceiver(sender , receiver);

        if (existingChatRoom.isPresent()) {
            return existingChatRoom.get().getChatId();
        }


        return createChatId(sender, receiver );
    }


    private Long createChatId( User sender, User recipient) {


        ChatRoom newChatRoom  = new ChatRoom();
        newChatRoom.setSender(sender);
        newChatRoom.setReceiver(recipient);
        chatRoomRepository.save(newChatRoom );


        return newChatRoom.getChatId();
    }
}




