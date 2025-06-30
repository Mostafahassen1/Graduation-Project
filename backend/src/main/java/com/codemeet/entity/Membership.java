package com.codemeet.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    @ManyToOne
    private User user;
    
    @ManyToOne
    private Room room;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MembershipStatus status;
    
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Instant joinedAt;
}
