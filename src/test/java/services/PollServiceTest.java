package services;

import org.example.dtos.PollRequestDto;
import org.example.entities.Poll;
import org.example.entities.Ring;
import org.example.entities.Technology;
import org.example.exceptions.RingNotFoundException;
import org.example.exceptions.TechnologyNotFoundException;
import org.example.repositories.PollRepository;
import org.example.repositories.RingRepository;
import org.example.repositories.TechnologyRepository;
import org.example.services.PollService;
import org.example.utils.JwtTokenUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PollServiceTest {
    @Mock
    private PollRepository pollRepository;
    @Mock
    private TechnologyRepository technologyRepository;
    @Mock
    private RingRepository ringRepository;
    @Mock
    private JwtTokenUtils jwtTokenUtils;
    @InjectMocks
    private PollService pollService;

    PollRequestDto pollRequestDto;
    @BeforeEach
    public void SetUp(){
        pollRequestDto = new PollRequestDto(1L, "ADOPT");
    }

    @Test
    public void successfulSavePoll(){
        Ring ring1 = new Ring(3L, "ADOPT");
        Ring ring2 = new Ring(4L, "TRIAL");
        LocalDateTime date = LocalDateTime.of(
                2024,
                12,
                12,
                12,
                30, 30,
                544019
        );
        Technology technology = new Technology(
                pollRequestDto.getTechId(),
                1L,
                1L,
                ring2,
                "Java",
                "description",
                1L,
                date
        );
        Long userId = 1L;
        Poll poll1 = new Poll(null, userId, technology, ring1, date);
        String token = "token";

        when(technologyRepository.findById(pollRequestDto.getTechId())).thenReturn(Optional.of(technology));
        when(ringRepository.findByName(pollRequestDto.getRingResult())).thenReturn(Optional.of(ring1));
        when(jwtTokenUtils.extractTokenFromContext()).thenReturn(token);
        when(jwtTokenUtils.getId(token)).thenReturn(userId);

        try (MockedStatic<LocalDateTime> mockedDateTime = mockStatic(LocalDateTime.class)) {
            mockedDateTime.when(LocalDateTime::now).thenReturn(date);
            pollService.savePoll(pollRequestDto);
        }
        verify(pollRepository).save(poll1);
    }

    @Test
    public void savePollWithTechnologyNotFoundException(){
        when(technologyRepository.findById(pollRequestDto.getTechId())).thenThrow(
                new TechnologyNotFoundException());

        Assertions.assertThrows(TechnologyNotFoundException.class, () ->
                pollService.savePoll(pollRequestDto));
    }

    @Test
    public void savePollWithRingNotFoundException(){
        Ring ring = new Ring(1L, "ADOPT");
        Technology technology = new Technology(
                1L,
                1L,
                1L,
                ring,
                "Java",
                "description",
                1L,
                LocalDateTime.of(2024, 12, 12, 12, 30, 30, 544019)
        );

        when(technologyRepository.findById(pollRequestDto.getTechId())).thenReturn(Optional.of(technology));
        when(ringRepository.findByName(pollRequestDto.getRingResult())).thenReturn(Optional.empty());

        Assertions.assertThrows(RingNotFoundException.class, () ->
                pollService.savePoll(pollRequestDto));
    }
}
