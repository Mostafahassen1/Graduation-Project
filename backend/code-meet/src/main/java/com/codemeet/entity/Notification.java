package com.codemeet.entity;


import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.Map;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @ElementCollection
    @CollectionTable(
        name = "notifications_info",
        joinColumns = @JoinColumn(name = "notification_id")
    )
    @MapKeyColumn(name = "info")
    @Column(name = "value")
    private Map<String, String> info;
    
    @ManyToOne
    @JoinColumn(nullable = false)
    private User receiver;
    
    @Column(nullable = false)
    private NotificationType type;
    
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Instant sentAt;
    
    public Notification() {
    }
    
    public int getId() {
        return id;
    }
    
    public Map<String, String> getInfo() {
        return info;
    }
    
    public void setInfo(Map<String, String> info) {
        this.info = info;
    }
    
    public User getReceiver() {
        return receiver;
    }
    
    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }
    
    public NotificationType getType() {
        return type;
    }
    
    public void setType(NotificationType type) {
        this.type = type;
    }
    
    public Instant getSentAt() {
        return sentAt;
    }
}
