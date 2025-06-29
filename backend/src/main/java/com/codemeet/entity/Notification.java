package com.codemeet.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.Map;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
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
}
