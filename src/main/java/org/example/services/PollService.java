package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dtos.PollRequestDto;
import org.example.entities.Poll;
import org.example.entities.Ring;
import org.example.entities.Technology;
import org.example.exceptions.RingNotFoundException;
import org.example.exceptions.TechnologyNotFoundException;
import org.example.repositories.PollRepository;
import org.example.repositories.RingRepository;
import org.example.repositories.TechnologyRepository;
import org.example.utils.JwtTokenUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PollService {
    private final PollRepository pollRepository;
    private final RingRepository ringRepository;
    private final TechnologyRepository technologyRepository;
    private final JwtTokenUtils jwtTokenUtils;
    public void savePoll(PollRequestDto poolRequestDto){
        LocalDateTime currentDate = LocalDateTime.now();
        String token = jwtTokenUtils.extractTokenFromContext();
        Optional<Ring> foundOptionalRing = ringRepository.findByName(poolRequestDto.getRingResult());
        Optional<Technology> foundOptionalTechnology = technologyRepository.findById(poolRequestDto.getTechId());

        Poll poll = new Poll();
        poll.setUserId(jwtTokenUtils.getId(token));
        poll.setTechnology(foundOptionalTechnology
                .orElseThrow(() ->
                        new TechnologyNotFoundException("Technology not found for ID: " + poolRequestDto.getTechId())
        ));
        poll.setRing(foundOptionalRing
                .orElseThrow(() ->
                        new RingNotFoundException("The specified = " + poolRequestDto.getRingResult() + " doesn't exist"))
                );
        poll.setTime(currentDate);

        pollRepository.save(poll);
    }
}
