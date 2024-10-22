package org.example.repositories;

import org.example.entities.Poll;
import org.example.entities.Technology;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PollRepository extends JpaRepository<Poll, Long> {
    @Query("""
        SELECT p FROM Poll p JOIN (
            SELECT p2.userId AS userId, MAX(p2.time) AS maxTime
            FROM Poll p2
            WHERE p2.technology = :technology
            GROUP BY p2.userId
        ) latest ON p.userId = latest.userId AND p.time = latest.maxTime
        WHERE p.technology = :technology
        """)
    List<Poll> findLastVotes(@Param("technology") Technology technology);
}
