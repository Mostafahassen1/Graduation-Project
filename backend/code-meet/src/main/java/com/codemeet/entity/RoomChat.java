package com.codemeet.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "room_chats")
public class RoomChat extends Chat {
    
    @OneToOne
    @JoinColumn(nullable = false)
    private Room room;
    
    public Room getRoom() {
        return room;
    }
    
    public void setRoom(Room room) {
        this.room = room;
    }
}
