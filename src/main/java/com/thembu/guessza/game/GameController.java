package com.thembu.guessza.game;


import com.thembu.guessza.game.dto.CreateGameRequest;
import com.thembu.guessza.game.dto.GameResponse;
import com.thembu.guessza.round.RoundService;
import com.thembu.guessza.round.dto.RoundResponse;
import org.springframework.http.HttpStatus;
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
    public GameResponse createGame(@RequestBody CreateGameRequest request) {
        return  gameService.createGame(request, "Standard");
    }

    @PostMapping("/province")
    @ResponseStatus(HttpStatus.CREATED)
    public  GameResponse createProvinceGame(@RequestBody CreateGameRequest request){
        return  gameService.createGame(request , "Province");
    }

    @GetMapping("/{id}/current-round")
    @ResponseStatus(HttpStatus.OK)
    public RoundResponse getCurrentRound(@PathVariable UUID id) {
        return roundService.getCurrentRound(id);
    } 

}
