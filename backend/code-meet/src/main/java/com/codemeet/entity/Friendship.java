package com.codemeet.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "friendships",uniqueConstraints = {
        @UniqueConstraint(name = "FRIENDSHIP_UNIQUE",columnNames = {"from_id","to_id"})
})
public class Friendship {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User from;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User to;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FriendshipStatus status;
    
    public Friendship(User from, User to, FriendshipStatus status) {
        this.from = from;
        this.to = to;
        this.status = status;
    }

    public Friendship() {
    }

    public Integer getId() {
        return id;
    }
    
    public User getFrom() {
        return from;
    }
    
    public void setFrom(User from) {
        this.from = from;
    }

    public User getTo() {
        return to;
    }

    public void setTo(User to) {
        this.to = to;
    }

    public FriendshipStatus getStatus() {
        return status;
    }
    
    public void setStatus(FriendshipStatus status) {
        this.status = status;
    }
}
