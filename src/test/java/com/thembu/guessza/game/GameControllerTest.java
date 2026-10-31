package com.thembu.guessza.game;


import com.thembu.guessza.round.RoundFromCompletedGame;
import com.thembu.guessza.round.RoundService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = GameController.class)
class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GameService gameService;

    @MockitoBean
    private RoundService roundService;

    @Test
    void unknownGameReturns404() throws Exception {
        UUID id = UUID.randomUUID();
        when(roundService.getCurrentRound(id)).thenThrow(new GameNotFoundException(id));

        mockMvc.perform(get("/api/games/{id}/current-round", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void completedGameReturns410() throws Exception {
        UUID id = UUID.randomUUID();
        when(roundService.getCurrentRound(id)).thenThrow(new RoundFromCompletedGame(id));

        mockMvc.perform(get("/api/games/{id}/current-round", id))
                .andExpect(status().isGone());
    }

    //errors come back as RFC 7807 problem details, so the frontend can rely on one JSON shape
    @Test
    void errorBodyIsProblemDetail() throws Exception {
        UUID id = UUID.randomUUID();
        when(roundService.getCurrentRound(id)).thenThrow(new GameNotFoundException(id));

        mockMvc.perform(get("/api/games/{id}/current-round", id))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detail").value("Game " + id + " Not Found"));
    }

    @Test
    void malformedGameIdReturns400() throws Exception {
        mockMvc.perform(get("/api/games/{id}/current-round", "not-a-uuid"))
                .andExpect(status().isBadRequest());
    }

}
