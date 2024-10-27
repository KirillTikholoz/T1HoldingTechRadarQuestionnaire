package controllers;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.example.common.Dashboard;
import org.example.configs.JwtDecoderConfig;
import org.example.configs.SecurityConfig;
import org.example.controllers.DashboardController;
import org.example.exceptions.TechnologyNotFoundException;
import org.example.filters.JwtRequestFilter;
import org.example.services.DashboardService;
import org.example.utils.JwtTokenUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DashboardController.class)
@ContextConfiguration(classes = {
        DashboardController.class,
        SecurityConfig.class,
        JwtDecoderConfig.class,
        JwtRequestFilter.class,
        JwtTokenUtils.class
})
public class DashboardControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private DashboardService dashboardService;
    @Value("${jwt.secret-key}")
    private String secretKey;
    @Test
    public void successfulCreateDashboard() throws Exception{
        long techId = 1L;
        String validToken =
                Jwts
                        .builder()
                        .subject("1")
                        .expiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                        .claim("role", "ADMIN")
                        .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)))
                        .compact();
        Map<String, Integer> votes = new HashMap<>(
                Map.of(
                        "ADOPT", 1,
                        "TRIAL", 2,
                        "HOLD", 3
                )
        );
        Dashboard dashboard = new Dashboard(
                1L,
                "name",
                "cat",
                "sec",
                "ADOPT",
                votes
        );

        when(dashboardService.createDashboard(techId)).thenReturn(dashboard);

        mockMvc.perform(get("/api/dashboard/{tech_id}", techId)
                        .header("Authorization", "Bearer " + validToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tech_id").value(1L))
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.category").value("cat"))
                .andExpect(jsonPath("$.section").value("sec"))
                .andExpect(jsonPath("$.ring").value("ADOPT"))
                .andExpect(jsonPath("$.votes").isMap())
                .andExpect(jsonPath("$.votes.ADOPT").value(1))
                .andExpect(jsonPath("$.votes.TRIAL").value(2))
                .andExpect(jsonPath("$.votes.HOLD").value(3));
    }

    @Test
    public void successfulCreateDashboardWithNotValidToken() throws Exception{
        long techId = 1L;
        String notValidToken = "23124434";

        mockMvc.perform(get("/api/dashboard/{tech_id}", techId)
                        .header("Authorization", "Bearer " + notValidToken))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    public void createDashboardWithAnotherAuthorities() throws Exception{
        long techId = 1L;

        mockMvc.perform(get("/api/dashboard/{tech_id}", techId))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void createDashboardWithTechnologyNotFoundException() throws Exception {
        long techId = 999L;

        when(dashboardService.createDashboard(techId)).thenThrow(new TechnologyNotFoundException());

        mockMvc.perform(get("/api/dashboard/{tech_id}", techId))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void createDashboardWithConstraintViolationException() throws Exception {
        long techId = -1;

        mockMvc.perform(get("/api/dashboard/{tech_id}", techId))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void createDashboardWithInternalServerError() throws Exception {
        long techId = 999L;

        when(dashboardService.createDashboard(techId)).thenThrow(new RuntimeException());

        mockMvc.perform(get("/api/dashboard/{tech_id}", techId))
                .andExpect(status().isInternalServerError());
    }
}
