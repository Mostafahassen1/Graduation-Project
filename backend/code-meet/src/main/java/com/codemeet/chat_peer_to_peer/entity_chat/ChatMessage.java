
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
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JoinColumn(name = "chat_id", nullable = false )
    private Long chatId;


    @JsonProperty("senderId")
    @ManyToOne
    @JoinColumn(name = "sender" , referencedColumnName = "username")
    private User sender;


    @JsonProperty("recipientId")
    @JoinColumn(name = "receiver" , referencedColumnName = "username")
    @ManyToOne
    private User receiver;


    @JsonProperty("content")
    private String message;

    private LocalDateTime timestamp;

    public ChatMessage() {
    }

    public ChatMessage(Long chatId, User sender, User receiver, String message, LocalDateTime timestamp) {
        this.chatId = chatId;
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.timestamp = timestamp;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public Long getChatId() {
        return chatId;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}




