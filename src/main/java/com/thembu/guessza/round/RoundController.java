package com.thembu.guessza.round;


import com.thembu.guessza.round.dto.GuessRequest;
import com.thembu.guessza.round.dto.GuessResponse;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/api/rounds")
public class RoundController {

    private final RoundService roundService;

    public RoundController(RoundService roundService) {
        this.roundService = roundService;
    }

    @PostMapping("/{roundId}/guess")
    @ResponseStatus(HttpStatus.OK)
    public GuessResponse submitGuess(@PathVariable UUID roundId , @RequestBody GuessRequest request) {

        return roundService.submitGuess(roundId , request);

    }
}
