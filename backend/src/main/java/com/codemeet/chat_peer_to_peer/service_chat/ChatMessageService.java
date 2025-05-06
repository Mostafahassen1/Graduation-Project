
package com.codemeet.chat_peer_to_peer.service_chat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.codemeet.chat_peer_to_peer.entity_chat.ChatMessage;
import com.codemeet.chat_peer_to_peer.entity_chat.ChatRoom;
import com.codemeet.chat_peer_to_peer.repository_chat.ChatMessageRepository;
import com.codemeet.chat_peer_to_peer.repository_chat.ChatRoomRepository;
import com.codemeet.entity.User;

@Service
public class ChatMessageService {


    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomService chatRoomService;
    private final ChatRoomRepository chatRoomRepository;

    public ChatMessageService(ChatMessageRepository chatMessageRepository, ChatRoomService chatRoomService, ChatRoomRepository chatRoomRepository) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatRoomService = chatRoomService;
        this.chatRoomRepository = chatRoomRepository;
    }

    public ChatMessage save(ChatMessage chatMessage){
       // System.out.println("The message from chatMessageService to save the message");
      //  System.out.println(chatMessage.getSender());
//        System.out.println(chatMessage.getReceiver() );
//        System.out.println(chatMessage.getMessage()) ;
//        System.out.println("------------------------------------------");
     Long chatId = chatRoomService.getChatRoomId( chatMessage.getSender(),chatMessage.getReceiver() ) ;

        chatMessage.setChatId(chatId);
        return chatMessageRepository.save(chatMessage);
    }


  public List<ChatMessage> findChatMessages(User sender, User receiver) {


        Optional<ChatRoom> chatRoom1 = chatRoomRepository.findBySenderAndReceiver(sender, receiver); // sender -> receiver
        Optional<ChatRoom> chatRoom2 = chatRoomRepository.findByReceiverAndSender(sender, receiver); // receiver -> sender


        List<ChatMessage> messages = new ArrayList<>();

        chatRoom1.ifPresent(room -> messages.addAll(chatMessageRepository.findAllByChatId(room.getChatId())));
        chatRoom2.ifPresent(room -> messages.addAll(chatMessageRepository.findAllByChatId(room.getChatId())));


        messages.sort(Comparator.comparing(ChatMessage::getTimestamp));


        return messages;
    }


}




