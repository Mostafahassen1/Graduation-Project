package com.codemeet.entity;

import jakarta.persistence.*;

@Entity
@Table(
    name = "participants",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "PARTICIPANT_UNIQUE",
            columnNames = {"user_id", "meeting_id"}
        )
    }
)
public class Participant {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Meeting meeting;

    public Participant(User user, Meeting meeting) {
        this.user = user;
        this.meeting = meeting;
    }

    public Participant() {
    }

    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public Meeting getMeeting() {
        return meeting;
    }
    
    public void setMeeting(Meeting meeting) {
        this.meeting = meeting;
    }
}
