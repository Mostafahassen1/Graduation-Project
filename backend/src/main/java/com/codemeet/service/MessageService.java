package com.codemeet.service;

import com.codemeet.entity.*;
import com.codemeet.repository.MessageRepository;
import com.codemeet.utils.dto.chat.*;
import com.codemeet.utils.dto.notification.NotificationInfoResponse;
import com.codemeet.utils.exception.IllegalActionException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.*;

import static com.codemeet.entity.NotificationType.*;

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
                    
                    // Send notification to the peer...
                    Map<String, Object> info = new LinkedHashMap<>();
                    info.put("chatId", pc2.getId());
                    info.put("senderFirstName", m2.getSender().getFirstName());
                    info.put("senderLastName", m2.getSender().getLastName());
                    info.put("senderUsername", m2.getSender().getUsername());
                    info.put("content", m2.getContent());
                    notificationService.sendToUser(new NotificationInfoResponse(
                        info, pc2.getOwner().getId(), PEER_MESSAGE
                    ));
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
                    for (RoomChat rci : roomChats) {
                        if (rci.getOwner().getId().equals(messages.get(0).getSender().getId())) continue;
                        
                        Map<String, Object> info = new LinkedHashMap<>();
                        info.put("chatId", messages.get(0).getChat().getId());
                        info.put("roomName", ((RoomChat) messages.get(0).getChat()).getRoom().getName());
                        info.put("senderFirstName", messages.get(0).getSender().getFirstName());
                        info.put("senderLastName", messages.get(0).getSender().getLastName());
                        info.put("senderUsername", messages.get(0).getSender().getUsername());
                        info.put("content", messages.get(0).getContent());
                        notificationService.sendToUser(new NotificationInfoResponse(
                            info, rci.getOwner().getId(), ROOM_MESSAGE
                        ));
                    }
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
