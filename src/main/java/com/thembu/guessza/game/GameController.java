package com.thembu.guessza.game;


import com.thembu.guessza.game.dto.CreateGameRequest;
import com.thembu.guessza.game.dto.GameResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/games")
@CrossOrigin
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public GameResponse createGame(@RequestBody CreateGameRequest request) {
        return  gameService.createGame(request);
    }

}
