package org.example.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "polls")
public class Poll {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "poll_id")
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    @ManyToOne
    @JoinColumn(name = "tech_id", referencedColumnName = "tech_id", nullable = false)
    private Technology technology;
    @ManyToOne
    @JoinColumn(name = "ring_id", referencedColumnName = "ring_id", nullable = false)
    private Ring ring;
    @Column(name = "time")
    private LocalDateTime time;
}
