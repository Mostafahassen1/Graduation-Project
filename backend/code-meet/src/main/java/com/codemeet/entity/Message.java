package com.codemeet.entity;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Entity
@Table(name = "messages")
public class Message {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(nullable = false)
    private Chat chat;
    
    @ManyToOne
    @JoinColumn(nullable = false)
    private User sender;
    
    @Column(nullable = false)
    private String content;
    
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime sentAt;
    

    
    @Override
    public String toString() {
        return "[%s\t%s (%s)\t%s]"
            .formatted(
                sentAt,
                sender.getFullName(),
                sender.getUsername(),
                content
            );
    }
}
