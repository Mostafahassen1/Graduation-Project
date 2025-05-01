package com.codemeet.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Entity
@Table(name = "peer_chats")
public class PeerChat extends Chat {
    
    @ManyToOne
    @JoinColumn(nullable = false)
    private User peer;
    
    public User getPeer() {
        return peer;
    }
    
    public void setPeer(User peer) {
        this.peer = peer;
    }
}
