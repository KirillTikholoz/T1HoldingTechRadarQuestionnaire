package services;

import org.example.common.Dashboard;
import org.example.entities.Poll;
import org.example.entities.Ring;
import org.example.entities.Technology;
import org.example.exceptions.TechnologyNotFoundException;
import org.example.repositories.PollRepository;
import org.example.repositories.TechnologyRepository;
import org.example.services.DashboardService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DashboardServiceTest {
    @Mock
    private PollRepository pollRepository;
    @Mock
    private TechnologyRepository technologyRepository;
    @InjectMocks
    private DashboardService dashboardService;

    @Test
    public void successfulCreateDashboard(){
        Ring ring1 = new Ring(1L, "ADOPT");
        Ring ring2 = new Ring(2L, "HOLD");
        Ring ring3 = new Ring(3L, "TRIAL");
        Long techId = 60L;
        String name = "Java";
        LocalDateTime date = LocalDateTime.of(
                2024,
                12,
                12,
                12,
                30, 30,
                544019
        );
        Technology technology = new Technology(
                techId,
                1L,
                1L,
                ring1,
                name,
                "description",
                1L,
                date
        );
        Poll poll1 = new Poll(1L, 1L, technology, ring2, date);
        Poll poll2 = new Poll(2L, 2L, technology, ring3, date);
        Map<String, Integer> votes = new HashMap<>();
        votes.put(ring2.getName(), 1);
        votes.put(ring3.getName(), 1);
        Dashboard dashboard = new Dashboard(
                techId,
                name,
                "CATEGORY_PLUG",
                "SECTION_PLUG",
                ring1.getName(),
                votes
        );

        when(technologyRepository.findById(techId)).thenReturn(Optional.of(technology));
        when(pollRepository.findLastVotes(technology)).thenReturn(List.of(poll1, poll2));

        Assertions.assertEquals(dashboard, dashboardService.createDashboard(techId));
    }

    @Test
    public void createDashboardWithTechnologyNotFoundException(){
        long techId = 60L;

        when(technologyRepository.findById(techId)).thenReturn(Optional.empty());

        Assertions.assertThrows(TechnologyNotFoundException.class, () ->
                dashboardService.createDashboard(techId));
    }

}
