package com.codemeet.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "room_chats")
public class RoomChat extends Chat {
    
    @ManyToOne
    @JoinColumn(nullable = false)
    private Room room;
}
