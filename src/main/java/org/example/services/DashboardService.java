package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.common.Dashboard;
import org.example.entities.Poll;
import org.example.entities.Technology;
import org.example.exceptions.TechnologyNotFoundException;
import org.example.repositories.PollRepository;
import org.example.repositories.TechnologyRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final PollRepository pollRepository;
    private final TechnologyRepository technologyRepository;
    private static final String CATEGORY_PLUG = "CATEGORY_PLUG";
    private static final String SECTION_PLUG = "SECTION_PLUG";
    public Dashboard createDashboard(long techId){
        Optional<Technology> optionalFoundTechnology = technologyRepository.findById(techId);
        Technology foundTechnology = optionalFoundTechnology.orElseThrow(() ->
                new TechnologyNotFoundException("Technology not found for ID: " + techId));

        Map<String, Integer> votes = findVotes(foundTechnology);

        return new Dashboard(foundTechnology.getId(),
                foundTechnology.getName(),
                CATEGORY_PLUG,
                SECTION_PLUG,
                foundTechnology.getRing().getName(),
                votes);
    }

    private Map<String, Integer> findVotes(Technology technology){
        Map<String, Integer> votes = new HashMap<>();
        List<Poll> polls = pollRepository.findLastVotes(technology);
        for (Poll poll: polls){
            String key = poll.getRing().getName();
            votes.put(key, votes.getOrDefault(key, 0) + 1);
        }

        return votes;
    }
}
