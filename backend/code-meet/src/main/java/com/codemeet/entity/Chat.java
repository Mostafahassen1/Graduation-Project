package com.codemeet.entity;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "chats")
public abstract class Chat {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(nullable = false)
    private User owner;
    
    @OneToOne
    private Message lastSentMessage;
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public User getOwner() {
        return owner;
    }
    
    public void setOwner(User owner) {
        this.owner = owner;
    }
    
    public Message getLastSentMessage() {
        return lastSentMessage;
    }
    
    public void setLastSentMessage(Message lastSent) {
        this.lastSentMessage = lastSent;
    }
}
