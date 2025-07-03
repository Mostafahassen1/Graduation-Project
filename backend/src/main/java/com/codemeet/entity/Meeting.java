package com.codemeet.entity;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "meetings")
public class Meeting {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false, length = 50)
    private String title;
    
    private String description;
    
    @ManyToOne
    @JoinColumn(nullable = false)
    private User creator;
    
    @Column(nullable = false)
    private Instant startsAt;
    
    @Column(nullable = false)
    private boolean isInstant;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MeetingStatus status;
}

