package com.codemeet.entity;


import com.codemeet.entity.user.User;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "memberships")
public class Membership {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private User user;
    
    @ManyToOne
    private Room room;
    
    @Column(nullable = false, updatable = false)
    private Instant joinedAt;
    
    public Membership() {
        this.joinedAt = Instant.now();
    }
    
    public Integer getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    public Room getRoom() {
        return room;
    }
    
    public void setRoom(Room room) {
        this.room = room;
    }
    
    public Instant getJoinedAt() {
        return joinedAt;
    }
}
