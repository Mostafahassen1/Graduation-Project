package com.codemeet.service;

import com.codemeet.entity.*;
import com.codemeet.repository.MessageRepository;
import com.codemeet.utils.dto.chat.*;
import com.codemeet.utils.exception.IllegalActionException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.*;

@RequiredArgsConstructor
@Service
public class MessageService {
    
    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationService notificationService;
    private final MessageRepository messageRepository;
    private final MembershipService membershipService;
    private final FriendshipService friendshipService;
    private final ChatService chatService;
    private final UserService userService;
    private final RoomService roomService;
    
    public Message save(Message message) {
        return messageRepository.save(message);
    }
    
    public List<Message> saveAll(List<Message> messages) {
        return messageRepository.saveAll(messages);
    }
    
    public List<Message> getAllMessageEntitiesOfPeerChatByChatId(Integer chatId) {
        // Checking if it is actually a peer chat (not practical way)...
        chatService.getPeerChatEntityById(chatId);
        return messageRepository.findAllByChatId(chatId);
    }
    
    public List<Message> getAllMessageEntitiesOfRoomChatByChatId(Integer chatId) {
        // Checking if it is actually a room chat (not practical way)...
        chatService.getRoomChatEntityById(chatId);
        return messageRepository.findAllByChatId(chatId);
    }
    
    @Transactional
    public synchronized void saveAndSendToPeer(PeerMessageRequest messageRequest) {
        PeerChat pc1 = chatService.getPeerChatEntityByOwnerIdAndPeerId(
            messageRequest.ownerId(),
            messageRequest.peerId()
        );
        User sender = userService.getUserEntityById(messageRequest.ownerId());
        
        PeerChat pc2 = chatService.getPeerChatEntityByOwnerIdAndPeerId(
            pc1.getPeer().getId(), pc1.getOwner().getId());
        
        Message m1 = Message.builder()
            .chat(pc1)
            .sender(sender)
            .content(messageRequest.content())
            .build();
        
        
        Message m2 = Message.builder()
            .chat(pc2)
            .sender(sender)
            .content(messageRequest.content())
            .build();
        
        pc1.setLastSentMessage(m1);
        pc2.setLastSentMessage(m2);
        
        saveAll(List.of(m1, m2));
        
        TransactionSynchronizationManager.registerSynchronization(
            new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    messagingTemplate.convertAndSend(
                        "/peer-chat/" + pc1.getPeer().getId(),
                        PeerMessageResponse.of(m1)
                    );
                    messagingTemplate.convertAndSend(
                        "/peer-chat/" + pc2.getPeer().getId(),
                        PeerMessageResponse.of(m2)
                    );
                    System.out.println("Sending peer message notification...");
                    
                    // Send notification to the peer...
                    notificationService.sendPeerMessageNotification(m2);
                }
            }
        );
    }
    
    @Transactional
    public synchronized void saveAndSendToRoom(RoomMessageRequest messageRequest) {
        RoomChat rc = chatService.getRoomChatEntityByOwnerIdAndRoomId(
            messageRequest.ownerId(),
            messageRequest.roomId()
        );
        User sender = userService.getUserEntityById(messageRequest.ownerId());
        
        // Check if the sender is a member of this room chat.
        if (!membershipService.exists(sender.getId(), rc.getRoom().getId())) {
            throw new IllegalActionException(
                "User with id '%d' is not a member of room with id '%d'"
                    .formatted(rc.getOwner().getId(), rc.getRoom().getId()));
        }
        
        List<Message> messages = new ArrayList<>();
        List<RoomChat> roomChats =
            chatService.getAllRoomChatEntitiesByRoomId(rc.getRoom().getId());
        
        for (RoomChat rci : roomChats) {
            Message mi = Message.builder()
                .chat(rci)
                .sender(sender)
                .content(messageRequest.content())
                .build();
            rci.setLastSentMessage(mi);
            messages.add(mi);
        }
        
        saveAll(messages);
        
        TransactionSynchronizationManager.registerSynchronization(
            new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    messagingTemplate.convertAndSend(
                        "/room-chat/" + rc.getRoom().getId(),
                        RoomMessageResponse.of(messages.get(0))
                    );
                    
                    // Send notification to all room members...
                    notificationService.sendRoomMessageNotification(messages);
                }
            }
        );
    }
    
    public List<PeerMessageResponse> getAllMessagesOfPeerChatByChatId(Integer chatId) {
        return getAllMessageEntitiesOfPeerChatByChatId(chatId).stream()
            .map(PeerMessageResponse::of)
            .toList();
    }
    
    public List<RoomMessageResponse> getAllMessagesOfRoomChatByChatId(Integer chatId) {
        return getAllMessageEntitiesOfRoomChatByChatId(chatId).stream()
            .map(RoomMessageResponse::of)
            .toList();
    }
}
