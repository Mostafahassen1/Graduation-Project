package com.codemeet.service;

import com.codemeet.entity.Chat;
import com.codemeet.entity.Message;
import com.codemeet.entity.MessageType;
import com.codemeet.entity.User;
import com.codemeet.repository.MessageRepository;
import com.codemeet.utils.dto.chat.MessageInfo;
import com.codemeet.utils.exception.IllegalActionException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChatService chatService;
    private final RoomChatService roomChatService;
    private final PeerChatService peerChatService;
    private final UserService userService;

    public MessageService(
        MessageRepository messageRepository,
        ChatService chatService,
        RoomChatService roomChatService,
        PeerChatService peerChatService,
        UserService userService
    ) {
        this.messageRepository = messageRepository;
        this.chatService = chatService;
        this.roomChatService = roomChatService;
        this.peerChatService = peerChatService;
        this.userService = userService;
    }
    
    public Message save(Message message) {
        return messageRepository.save(message);
    }
    
    public List<Message> getAllMessageEntitiesByRoomId(Integer roomId) {
        return messageRepository.findAllByRoomId(roomId);
    }
    
    public List<Message> getAllMessageEntitiesByChatId(Integer chatId) {
        return messageRepository.findAllByChatId(chatId);
    }
    
    @Transactional
    public synchronized MessageInfo save(MessageInfo messageInfo) {
        Chat chat = chatService.getChatEntityById(messageInfo.chatId());
        User sender = userService.getUserEntityById(messageInfo.senderId());
        
        Message message =
            new Message(chat, sender, messageInfo.content(), messageInfo.type());
        
        if (message.getType() == MessageType.ROOM_MESSAGE) {
            //TODO: Check if the sender is a member of this chat.
            if (!roomChatService.isMemberOfChat(sender.getId(), chat.getId())) {
                throw new IllegalActionException(
                    "User with id '%d' is not member of chat with id '%d'"
                        .formatted(sender.getId(), chat.getId()));
            }
        } else {
        
        }
        
        chat.setLastSentMessage(message);
        
        return MessageInfo.of(messageRepository.save(message));
    }
    
    public List<MessageInfo> getAllMessagesOfRoom(Integer roomId) {
        return getAllMessageEntitiesByChatId(roomId).stream()
            .map(MessageInfo::of)
            .toList();
    }
    
    public List<MessageInfo> getAllMessagesOfChat(Integer chatId) {
        return getAllMessageEntitiesByChatId(chatId).stream()
            .map(MessageInfo::of)
            .toList();
    }
}
