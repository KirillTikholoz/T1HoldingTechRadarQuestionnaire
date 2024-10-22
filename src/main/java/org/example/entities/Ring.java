package org.example.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "rings")
public class Ring {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ring_id")
    private Long id;

    @Column(name = "ring_name")
    private String name;
}
