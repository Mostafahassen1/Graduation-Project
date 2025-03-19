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
    private Message lastSent;
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Message getLastSent() {
        return lastSent;
    }
    
    public void setLastSent(Message lastSent) {
        this.lastSent = lastSent;
    }
}
