package com.thembu.guessza.round;

import com.thembu.guessza.config.JwtUtil;
import com.thembu.guessza.config.security.SecurityConfig;
import com.thembu.guessza.user.UserRepository;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RoundController.class)
@Import(SecurityConfig.class) // run through the real security rules, not Boot's defaults
@WithMockUser(username = RoundControllerTest.USERNAME) // these endpoints all require a logged-in user
class RoundControllerTest {

    // the controller must pass the logged-in user's name through to the service
    static final String USERNAME = "player";

    // SecurityConfig and JwtFilter need these, and the web slice doesn't load them
    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private UserRepository userRepository;

    private static final String GUESS = """
            { "guessLatitude": -26.2, "guessLongitude": 28.05 }
            """;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RoundService roundService;

    @Test
    void unknownRoundReturns404() throws Exception {
        UUID id = UUID.randomUUID();
        when(roundService.submitGuess(eq(id), any(), eq(USERNAME))).thenThrow(new RoundNotFoundException(id));

        mockMvc.perform(post("/api/rounds/{id}/guess", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(GUESS))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Round " + id + " Not Found"));
    }

    @Test
    void alreadyAnsweredRoundReturns409() throws Exception {
        UUID id = UUID.randomUUID();
        when(roundService.submitGuess(eq(id), any(), eq(USERNAME))).thenThrow(new RoundAlreadyAnswered(id));

        mockMvc.perform(post("/api/rounds/{id}/guess", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(GUESS))
                .andExpect(status().isConflict());
    }

    @Test
    void versionConflictReturns409() throws Exception {
        UUID id = UUID.randomUUID();
        //plain parent type on purpose: the handler must catch every subclass, not just Hibernate's
        when(roundService.submitGuess(eq(id), any(), eq(USERNAME)))
                .thenThrow(new OptimisticLockingFailureException("stale round"));

        mockMvc.perform(post("/api/rounds/{id}/guess", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(GUESS))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail")
                        .value("The resource was modified by another request. Refresh and try again."));
    }

}
