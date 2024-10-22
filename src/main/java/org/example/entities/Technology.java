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
@Table(name = "technologies")
public class Technology {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tech_id")
    private Long id;
    @Column(name = "cat_id")
    private Long catId;
    @Column(name = "sec_id")
    private Long secId;
    @ManyToOne
    @JoinColumn(name = "ring_id", referencedColumnName = "ring_id", nullable = false)
    private Ring ring;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "stat_id")
    private Long statId;
    @Column(name = "update_time")
    private LocalDateTime updateTime;

}
