package org.example.hardcode.data.initializer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entities.Ring;
import org.example.repositories.RingRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class RingDataInitializer implements CommandLineRunner {
    private final RingRepository ringRepository;
    @Override
    public void run(String... args) {
        if (ringRepository.count() == 0) {
            List<Ring> rings = new LinkedList<>();
            rings.add(new Ring(1L, "ADOPT"));
            rings.add(new Ring(2L, "TRIAL"));
            rings.add(new Ring(3L, "ASSETS"));
            rings.add(new Ring(4L, "HOLD"));
            rings.add(new Ring(5L, "BACKLOG"));
            ringRepository.saveAll(rings);
            log.info("Initial rings data has been added to the database");
        } else {
            log.info("The ring database has already been initialized");
        }
    }
}
