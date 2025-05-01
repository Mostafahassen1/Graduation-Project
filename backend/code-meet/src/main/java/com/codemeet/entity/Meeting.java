package com.codemeet.entity;

import java.time.Instant;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Builder
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
    private LocalDateTime startsAt;

    @Column(nullable = false)
    private boolean isInstant;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MeetingStatus status;


}
