package com.codemeet.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "messages")
public class Message {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(nullable = false)
    private Chat chat;
    
    @ManyToOne
    @JoinColumn(nullable = false)
    private User sender;
    
    @Column(nullable = false)
    private String content;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    private MessageType type;
    
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Instant sentAt;
    
    public Message() {
    }
    
    public Message(Chat chat, User sender, String content, MessageType type) {
        this.chat = chat;
        this.sender = sender;
        this.content = content;
        this.type = type;
    }
    
    public Integer getId() {
        return id;
    }
    
    public Chat getChat() {
        return chat;
    }
    
    public void setChat(Chat chat) {
        this.chat = chat;
    }
    
    public User getSender() {
        return sender;
    }
    
    public void setSender(User sender) {
        this.sender = sender;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public MessageType getType() {
        return type;
    }
    
    public void setType(MessageType type) {
        this.type = type;
    }
    
    public Instant getSentAt() {
        return sentAt;
    }
}
