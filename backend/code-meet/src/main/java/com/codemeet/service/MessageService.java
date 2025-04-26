package com.codemeet.service;

import com.codemeet.entity.*;
import com.codemeet.repository.MessageRepository;
import com.codemeet.utils.dto.NotificationInfo;
import com.codemeet.utils.dto.chat.MessageInfoRequest;
import com.codemeet.utils.dto.chat.MessageInfoResponse;
import com.codemeet.utils.exception.IllegalActionException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.codemeet.entity.NotificationType.MEMBERSHIP_REQUEST;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final MembershipService membershipService;
    private final FriendshipService friendshipService;
    private final ChatService chatService;
    private final UserService userService;
    private final RoomService roomService;

    public MessageService(
        MessageRepository messageRepository,
        MembershipService membershipService,
        FriendshipService friendshipService,
        ChatService chatService,
        UserService userService,
        RoomService roomService
    ) {
        this.messageRepository = messageRepository;
        this.membershipService = membershipService;
        this.friendshipService = friendshipService;
        this.chatService = chatService;
        this.userService = userService;
        this.roomService = roomService;
    }
    
    public Message save(Message message) {
        return messageRepository.save(message);
    }
    
    public List<Message> saveAll(List<Message> messages) {
        return messageRepository.saveAll(messages);
    }
    
    public List<Message> getAllMessageEntitiesOfChatByChatId(Integer chatId) {
        return messageRepository.findAllByChatId(chatId);
    }
    
    @Transactional
    public synchronized MessageInfoResponse save(MessageInfoRequest messageInfo) {
        Chat chat = chatService.getChatEntityById(messageInfo.chatId());
        User sender = userService.getUserEntityById(messageInfo.senderId());
        
        if (!chat.getOwner().getId().equals(sender.getId())) {
            throw new IllegalActionException(
                "User with id '%d' is not allowed to send messages in chat with id '%d'"
                    .formatted(sender.getId(), chat.getId()));
        }
        
        if (chat instanceof RoomChat rc) {
            // Check if the sender is a member of this room chat.
            if (!membershipService.exists(rc.getOwner().getId(), rc.getRoom().getId())) {
                throw new IllegalActionException(
                    "User with id '%d' is not a member of room with id '%d'"
                        .formatted(rc.getOwner().getId(), rc.getRoom().getId()));
            }
            
            List<Message> messages = new ArrayList<>();
            List<RoomChat> roomChats = chatService.getAllRoomChatEntitiesByRoomId(rc.getRoom().getId());
            for (RoomChat rci : roomChats) {
                Message mi = new Message();
                mi.setChat(rci);
                mi.setSender(sender);
                mi.setContent(messageInfo.content());
                messages.add(mi);
            }
            
            saveAll(messages);
            
            TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        for (int i = 0; i < roomChats.size(); i++) {
                            roomChats.get(i).setLastSentMessage(messages.get(i));
                        }
                        
                        //TODO: Send notification to all room members...
                    }
                }
            );
            
            return MessageInfoResponse.of(messages.get(0));
        } else if (chat instanceof PeerChat pc1){
            // Check if the sender is a friend of the peer.
            if (!friendshipService.exists(pc1.getOwner().getId(), pc1.getPeer().getId())) {
                throw new IllegalActionException(
                    "User with id '%d' is not a friend of user with id '%d'"
                        .formatted(pc1.getOwner().getId(), pc1.getPeer().getId()));
            }
            
            PeerChat pc2 = chatService.getPeerChatEntityByOwnerIdAndPeerId(
                pc1.getPeer().getId(), pc1.getOwner().getId());
            
            Message m1 = new Message();
            m1.setChat(pc1);
            m1.setSender(sender);
            m1.setContent(messageInfo.content());
            
            Message m2 = new Message();
            m2.setChat(pc2);
            m2.setSender(sender);
            m2.setContent(messageInfo.content());
            
            saveAll(List.of(m1, m2));
            
            TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        pc1.setLastSentMessage(m1);
                        pc2.setLastSentMessage(m2);
                        
                        //TODO: Send notification to the peer...
                    }
                }
            );
            
            return MessageInfoResponse.of(m1);
        } else {
            throw new IllegalStateException("Impossible state");
        }
    }
    
    public List<MessageInfoResponse> getAllMessagesOfChatByChatId(Integer chatId) {
        return getAllMessageEntitiesOfChatByChatId(chatId).stream()
            .map(MessageInfoResponse::of)
            .toList();
    }
}
