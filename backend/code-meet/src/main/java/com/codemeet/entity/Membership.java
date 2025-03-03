package com.codemeet.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(
    name = "memberships",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "USER_ROOM_UNIQUE",
            columnNames = {"user_id", "room_id"}
        )
    }
)
public class Membership {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Room room;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MembershipStatus status;

    private Instant joinedAt;
    
    public Membership() {
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
    
    public MembershipStatus getStatus() {
        return status;
    }
    
    public void setStatus(MembershipStatus status) {
        this.status = status;
    }
    
    public Instant getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(Instant joinedAt) {
        this.joinedAt = joinedAt;
    }
}
