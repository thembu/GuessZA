package com.thembu.guessza.round;


import com.thembu.guessza.round.dto.GuessRequest;
import com.thembu.guessza.round.dto.GuessResponse;
import com.thembu.guessza.round.dto.RoundResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/api/rounds")
@SecurityRequirement(name = "bearerAuth")
public class RoundController {

    private final RoundService roundService;

    public RoundController(RoundService roundService) {
        this.roundService = roundService;
    }

    @PostMapping("/{roundId}/guess")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponse(responseCode = "200" , description = "Guess submitted successfully" , content = @Content(schema = @Schema(implementation = GuessResponse.class)))
    @ApiResponse(responseCode = "404", description = "Round not found", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "400", description = "Invalid guess coordinates", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "403", description = "Round belongs to another user", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "409", description = "Round already answered, or a concurrent guess won the race", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    public GuessResponse submitGuess(@PathVariable UUID roundId , @Valid @RequestBody GuessRequest request , @AuthenticationPrincipal UserDetails principal) {

        return roundService.submitGuess(roundId , request , principal.getUsername());

    }
}
