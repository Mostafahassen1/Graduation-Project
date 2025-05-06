
package com.codemeet.chat_peer_to_peer.entity_chat;

import java.time.LocalDateTime;

import com.codemeet.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long chatId;


     @JsonProperty("senderId")
     @ManyToOne
     @JoinColumn(name = "sender" , referencedColumnName = "username")
     private User sender;

    @JoinColumn(name = "receiver" , referencedColumnName = "username")
    @ManyToOne
    @JsonProperty("recipientId")
    private User receiver;

    private LocalDateTime LastMessageTimeStamp;

    public ChatRoom() {
    }

    public ChatRoom(Long chatId, User sender, User receiver, LocalDateTime lastMessageTimeStamp) {
        this.chatId = chatId;
        this.sender = sender;
        this.receiver = receiver;
        LastMessageTimeStamp = lastMessageTimeStamp;
    }


    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public LocalDateTime getLastMessageTimeStamp() {
        return LastMessageTimeStamp;
    }

    public void setLastMessageTimeStamp(LocalDateTime lastMessageTimeStamp) {
        LastMessageTimeStamp = lastMessageTimeStamp;
    }
}




