package com.thembu.guessza.game;


import com.thembu.guessza.game.dto.CreateGameRequest;
import com.thembu.guessza.game.dto.CreateProvinceGameRequest;
import com.thembu.guessza.game.dto.GameResponse;
import com.thembu.guessza.round.RoundNotFoundException;
import com.thembu.guessza.round.RoundService;
import com.thembu.guessza.round.dto.RoundResponse;
import com.thembu.guessza.user.UserNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/api/games")
@CrossOrigin
@SecurityRequirement(name = "bearerAuth")
public class GameController {

    private final GameService gameService;
    private  final RoundService roundService;

    public GameController(GameService gameService, RoundService roundService) {
        this.gameService = gameService;
        this.roundService = roundService;
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)

    //for documentation
    @Operation(summary = "Create Standard 5 round game")
    @ApiResponse(responseCode = "404", description = "Round not found", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "201" , description = "Standard game created" , content = @Content(schema = @Schema(implementation = GameResponse.class)))
    public GameResponse createGame(@AuthenticationPrincipal UserDetails principal) {
        return  gameService.createGame(principal.getUsername());
    }

    @PostMapping("/province")
    @ResponseStatus(HttpStatus.CREATED)

    @Operation(summary = "Create Province based game")
    @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "201" , description = "Province  game created" , content = @Content(schema = @Schema(implementation = GameResponse.class)))
    public  GameResponse createProvinceGame(@RequestBody @Valid CreateProvinceGameRequest request ,  @AuthenticationPrincipal UserDetails principal){
        return  gameService.createProvinceGame(request , principal.getUsername());
    }

    @GetMapping("/{id}/current-round")
    @ResponseStatus(HttpStatus.OK)

    @Operation(summary = "Fetch current round in game with {id}")
    @ApiResponse(responseCode = "200" , description = "Current round fetched successfully" , content = @Content(schema = @Schema(implementation = RoundResponse.class)))
    @ApiResponse(responseCode = "404", description = "Round not found", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    public RoundResponse getCurrentRound(@PathVariable UUID id , @AuthenticationPrincipal UserDetails principal) {
        return roundService.getCurrentRound(id , principal.getUsername());
    } 

}
