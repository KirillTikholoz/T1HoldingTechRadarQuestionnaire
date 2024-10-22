package org.example.repositories;

import org.example.entities.Ring;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RingRepository extends JpaRepository<Ring, Long> {
    Optional<Ring> findByName(String name);
}
