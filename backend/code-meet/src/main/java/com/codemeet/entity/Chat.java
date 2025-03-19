package com.codemeet.entity;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "chats")
public abstract class Chat {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @OneToOne
    private Message lastSentMessage;
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Message getLastSentMessage() {
        return lastSentMessage;
    }
    
    public void setLastSentMessage(Message lastSent) {
        this.lastSentMessage = lastSent;
    }
}
