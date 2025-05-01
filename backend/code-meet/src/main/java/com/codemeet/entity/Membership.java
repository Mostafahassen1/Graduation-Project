package com.codemeet.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
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

    private LocalDateTime joinedAt;
    

}
