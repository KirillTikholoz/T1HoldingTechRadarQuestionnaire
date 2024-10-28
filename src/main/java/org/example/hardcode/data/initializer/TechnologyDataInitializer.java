package org.example.hardcode.data.initializer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entities.Technology;
import org.example.repositories.RingRepository;
import org.example.repositories.TechnologyRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Component
@DependsOn("ringDataInitializer")
@RequiredArgsConstructor
@Slf4j
public class TechnologyDataInitializer implements CommandLineRunner {
    private final TechnologyRepository technologyRepository;
    private final RingRepository ringRepository;
    @Override
    public void run(String... args) {
        if (technologyRepository.count() == 0) {
            List<Technology> technologies = new LinkedList<>();
            technologies.add(
                    new Technology(1L,
                            1L,
                            1L,
                            ringRepository.findById(3L).orElseThrow(),
                            "Terraform",
                            "Инфраструктура как код",
                            1L,
                            LocalDateTime.of(2024, 8, 30, 22, 34, 34, 544019000))
            );
            technologies.add(
                    new Technology(2L,
                            2L,
                            2L,
                            ringRepository.findById(1L).orElseThrow(),
                            "Git",
                            "Система контроля версий",
                            3L,
                            LocalDateTime.of(2024, 8, 30, 22, 34, 34, 544019000))
            );
            technologies.add(
                    new Technology(3L,
                            3L,
                            3L,
                            ringRepository.findById(1L).orElseThrow(),
                            "Java",
                            "Язык программирования",
                            3L,
                            LocalDateTime.of(2024, 8, 30, 22, 34, 34, 544019000))
            );

            technologyRepository.saveAll(technologies);
            log.info("Initial technologies data has been added to the database");
        } else {
            log.info("The technology database has already been initialized");
        }
    }
}
