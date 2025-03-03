package com.codemeet.entity;

import java.time.Instant;

import jakarta.persistence.*;

@Entity
@Table(name = "meetings")
public class Meeting {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false, length = 50)
    private String title;
    
    private String description;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User creator;
    
    @Column(nullable = false)
    private Instant startsAt;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MeetingStatus status;

    public Meeting(
        String title,
        String description,
        User creator,
        Instant startsAt,
        MeetingStatus status
    ) {
        this.title = title;
        this.description = description;
        this.creator = creator;
        this.startsAt = startsAt;
        this.status = status;
    }

    public Meeting() {
    }
    
    public Integer getId() {
        return id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public User getCreator() {
        return creator;
    }
    
    public void setCreator(User creator) {
        this.creator = creator;
    }
    
    public Instant getStartsAt() {
        return startsAt;
    }
    
    public void setStartsAt(Instant startsAt) {
        this.startsAt = startsAt;
    }
    
    public MeetingStatus getStatus() {
        return status;
    }
    
    public void setStatus(MeetingStatus status) {
        this.status = status;
    }
}
