package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.example.configs.JwtDecoderConfig;
import org.example.configs.SecurityConfig;
import org.example.controllers.PollController;
import org.example.dtos.PollRequestDto;
import org.example.exceptions.RingNotFoundException;
import org.example.exceptions.TechnologyNotFoundException;
import org.example.filters.JwtRequestFilter;
import org.example.services.PollService;
import org.example.utils.JwtTokenUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PollController.class)
@ContextConfiguration(classes = {
        PollController.class,
        SecurityConfig.class,
        JwtDecoderConfig.class,
        JwtRequestFilter.class,
        JwtTokenUtils.class
})
public class PollControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private PollService pollService;
    @Value("${jwt.secret-key}")
    private String secretKey;

    String pollRequestDtoJson;
    PollRequestDto pollRequestDto;
    @BeforeEach
    public void setUp() throws Exception{
        pollRequestDto = new PollRequestDto(1L, "ADOPT");
        pollRequestDtoJson = objectMapper.writeValueAsString(pollRequestDto);
    }

    @Test
    public void successfulSavePoll() throws Exception{
        String validToken =
                Jwts
                        .builder()
                        .subject("1")
                        .expiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                        .claim("role", "USER")
                        .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)))
                        .compact();

        mockMvc.perform(post("/poll")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(pollRequestDtoJson)
                        .header("Authorization", "Bearer " + validToken))
                .andExpect(status().isCreated());
    }

    @Test
    public void savePollWithNotValidToken() throws Exception{
        String notValidToken = "23124434";

        mockMvc.perform(post("/poll")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(pollRequestDtoJson)
                        .header("Authorization", "Bearer " + notValidToken))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void savePollWithAnotherAuthorities() throws Exception{
        mockMvc.perform(post("/poll")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(pollRequestDtoJson))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    public void savePollWithTechnologyNotFoundException() throws Exception{
        doThrow(new TechnologyNotFoundException()).when(pollService).savePoll(pollRequestDto);

        mockMvc.perform(post("/poll")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(pollRequestDtoJson))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    public void savePollWithRingNotFoundException() throws Exception{
        doThrow(new RingNotFoundException()).when(pollService).savePoll(pollRequestDto);

        mockMvc.perform(post("/poll")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(pollRequestDtoJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    public void savePollWithMissingTechId() throws Exception{
        PollRequestDto pollRequestDtoWithMissingTechId = new PollRequestDto(null, "ADOPT");
        String pollRequestDtoDtoWithMissingTechIdJson =
                objectMapper.writeValueAsString(pollRequestDtoWithMissingTechId);

        mockMvc.perform(post("/poll")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(pollRequestDtoDtoWithMissingTechIdJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    public void savePollWithMissingRingResult() throws Exception{
        PollRequestDto pollRequestDtoWithMissingRingResult = new PollRequestDto(1L, "");
        String pollRequestDtoDtoWithMissingRingResultJson =
                objectMapper.writeValueAsString(pollRequestDtoWithMissingRingResult);

        mockMvc.perform(post("/poll")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(pollRequestDtoDtoWithMissingRingResultJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    public void savePollWithInternalServerError() throws Exception{
        doThrow(new RuntimeException()).when(pollService).savePoll(pollRequestDto);

        mockMvc.perform(post("/poll")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(pollRequestDtoJson))
                .andExpect(status().isInternalServerError());
    }
}
