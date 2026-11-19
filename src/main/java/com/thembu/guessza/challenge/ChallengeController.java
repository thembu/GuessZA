package com.thembu.guessza.challenge;


import com.thembu.guessza.challenge.dto.ChallengeResponse;
import com.thembu.guessza.game.dto.GameResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/challenges")
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin
public class ChallengeController {

    private final ChallengeService challengeService;

    public ChallengeController(ChallengeService challengeService) {
        this.challengeService = challengeService;
    }


    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create challenge multiplayer game")
    @ApiResponse(responseCode = "201" , description = "challenge  game created" , content = @Content(schema = @Schema(implementation = ChallengeResponse.class)))

    public ChallengeResponse createChallenge(@AuthenticationPrincipal UserDetails principal) {
        return challengeService.createChallenge(principal.getUsername());
    }

    @PostMapping("/{code}/play")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "start challenge multiplayer game")
    @ApiResponse(responseCode = "201" , description = "challenge  game started" , content = @Content(schema = @Schema(implementation = GameResponse.class)))
    @ApiResponse(responseCode = "404", description = "challenge not found", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "410", description = "challenge has expired", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))

    public  GameResponse startChallenge(@PathVariable String code , @AuthenticationPrincipal UserDetails principal) {
        return  challengeService.StartChallengeGame(code , principal.getUsername());
    }

}
