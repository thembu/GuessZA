package com.thembu.guessza.round;


import com.thembu.guessza.game.Game;
import com.thembu.guessza.game.GameNotFoundException;
import com.thembu.guessza.game.GameRepository;
import com.thembu.guessza.location.LocationRepository;
import com.thembu.guessza.round.dto.RoundResponse;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class RoundService {

    private final GameRepository gameRepository;
    private final RoundRepository roundRepository;

    public RoundService(GameRepository gameRepository, RoundRepository roundRepository) {
        this.gameRepository = gameRepository;
        this.roundRepository = roundRepository;
    }

    public RoundResponse getCurrentRound(UUID gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new GameNotFoundException(gameId));

        Round round  = roundRepository.findFirstByGameAndAnsweredAtIsNullOrderByRoundNumberAsc(game).orElseThrow(() -> new GameNotFoundException(gameId));

        return  new RoundResponse(round.getId(), round.getRoundNumber(), round.getLocation().getLatitude(), round.getLocation().getLongitude());

    }

}
