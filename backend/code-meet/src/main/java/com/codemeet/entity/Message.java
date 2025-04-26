package com.codemeet.entity;


import jakarta.persistence.*;
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
    
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Instant sentAt;
    
    public Message() {
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
    
    public Instant getSentAt() {
        return sentAt;
    }
    
    @Override
    public String toString() {
        return "[%s\t%s (%s)\t%s]"
            .formatted(
                sentAt,
                sender.getFullName(),
                sender.getUsername(),
                content
            );
    }
}
