package com.codemeet.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
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

    @Column(nullable = false)
    private boolean isParticipated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Meeting meeting;




}
