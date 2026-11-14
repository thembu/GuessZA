package com.thembu.guessza.game;


import com.thembu.guessza.game.dto.CreateGameRequest;
import com.thembu.guessza.game.dto.CreateProvinceGameRequest;
import com.thembu.guessza.game.dto.GameResponse;
import com.thembu.guessza.round.RoundService;
import com.thembu.guessza.round.dto.RoundResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/games")
@CrossOrigin
public class GameController {

    private final GameService gameService;
    private  final RoundService roundService;

    public GameController(GameService gameService, RoundService roundService) {
        this.gameService = gameService;
        this.roundService = roundService;
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public GameResponse createGame(@AuthenticationPrincipal @Valid UserDetails principal) {
        return  gameService.createGame(principal.getUsername());
    }

    @PostMapping("/province")
    @ResponseStatus(HttpStatus.CREATED)
    public  GameResponse createProvinceGame(@RequestBody @Valid CreateProvinceGameRequest request ,  @AuthenticationPrincipal UserDetails principal){
        return  gameService.createProvinceGame(request , principal.getUsername());
    }

    @GetMapping("/{id}/current-round")
    @ResponseStatus(HttpStatus.OK)
    public RoundResponse getCurrentRound(@PathVariable UUID id , @AuthenticationPrincipal UserDetails principal) {
        return roundService.getCurrentRound(id , principal.getUsername());
    } 

}
