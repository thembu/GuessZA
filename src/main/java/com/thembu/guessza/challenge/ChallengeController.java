package com.thembu.guessza.challenge;


import com.thembu.guessza.challenge.dto.ChallengeInfoResponse;
import com.thembu.guessza.challenge.dto.ChallengeResponse;
import com.thembu.guessza.challenge.dto.DailyLeaderboardResponse;
import com.thembu.guessza.game.dto.GameResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.dao.DataIntegrityViolationException;
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



    @GetMapping("/{code}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Challenge info and results (results only after you've finished)")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = ChallengeInfoResponse.class)))
    @ApiResponse(responseCode = "404", description = "challenge not found", content = @Content(mediaType = "application/problem+json", schema =
    @Schema(implementation = ProblemDetail.class)))
    public ChallengeInfoResponse getChallenge(@PathVariable String code, @AuthenticationPrincipal UserDetails principal) {
        return challengeService.getChallengeInfo(code, principal.getUsername());
    }

    @GetMapping("/daily")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get today's daily challenge (created on first request if the scheduled job hasn't run yet)")
    @ApiResponse(responseCode = "200", description = "today's daily challenge code and link", content = @Content(schema = @Schema(implementation = ChallengeResponse.class)))
    public  ChallengeResponse getDailyChallenge() {
        try {
            return challengeService.createDailyChallenge();
        } catch (DataIntegrityViolationException ex) {
            throw new DailyChallengeAlreadyCreatedException("Daily Challenge already created");
        }
    }

    @GetMapping("/daily/leaderboard")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Today's daily challenge leaderboard (finished games only, best score first; ties share a rank)")
    @ApiResponse(responseCode = "200", description = "today's ranking, empty if no daily exists yet or nobody has finished", content = @Content(schema = @Schema(implementation = DailyLeaderboardResponse.class)))
    public DailyLeaderboardResponse getDailyLeaderboard() {
        return challengeService.getDailyLeaderboard();
    }

}
