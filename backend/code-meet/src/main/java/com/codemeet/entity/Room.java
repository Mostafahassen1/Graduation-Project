package com.codemeet.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false)
    private String name;
    
    private String description;
    
    @ManyToOne
    @JoinColumn(nullable = false)
    private User creator;
    
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Instant createdAt;
    
    @Lob
    private byte[] roomPicture;

    public Room() {
    }
    
    public Integer getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public User getCreator() {
        return creator;
    }
    
    public void setCreator(User creator) {
        this.creator = creator;
    }
    
    public Instant getCreatedAt() {
        return createdAt;
    }
    
    public byte[] getRoomPicture() {
        return roomPicture;
    }
    
    public void setRoomPicture(byte[] roomPicture) {
        this.roomPicture = roomPicture;
    }
}
