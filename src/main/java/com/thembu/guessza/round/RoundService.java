package com.thembu.guessza.round;


import com.thembu.guessza.game.Game;
import com.thembu.guessza.game.GameNotFoundException;
import com.thembu.guessza.game.GameRepository;
import com.thembu.guessza.game.GameStatus;
import com.thembu.guessza.location.LocationRepository;
import com.thembu.guessza.location.Province;
import com.thembu.guessza.round.dto.*;
import com.thembu.guessza.user.User;
import com.thembu.guessza.user.UserNotFoundException;
import com.thembu.guessza.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RoundService {

    private final GameRepository gameRepository;
    private final RoundRepository roundRepository;
    private final ScoringService scoringService;
    private final UserRepository userRepository;

    public RoundService(GameRepository gameRepository, RoundRepository roundRepository, ScoringService scoringService, UserRepository userRepository) {
        this.gameRepository = gameRepository;
        this.roundRepository = roundRepository;
        this.scoringService = scoringService;
        this.userRepository = userRepository;
    }

    public RoundResponse getCurrentRound(UUID gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new GameNotFoundException(gameId));

        Round round  = roundRepository.findFirstByGameAndAnsweredAtIsNullOrderByRoundNumberAsc(game).orElseThrow(() -> new GameNotFoundException(gameId));

        return  new RoundResponse(round.getId(), round.getRoundNumber(), round.getLocation().getLatitude(), round.getLocation().getLongitude());

    }

    @Transactional
    public GuessResponse submitGuess(UUID roundId ,GuessRequest request) {
        //validate game & round  exists and is not answered
        Round round = roundRepository.findById(roundId).orElseThrow(() -> new RoundNotFoundException(roundId));

        if(round.getAnsweredAt() != null) throw  new RoundAlreadyAnswered(roundId);

        Game game = gameRepository.findById(round.getGame().getId()).orElseThrow(() -> new GameNotFoundException(round.getGame().getId()));

        User user  = userRepository.findById(game.getUser().getId()).orElseThrow(() -> new UserNotFoundException(game.getUser().getId()));

        //save both locations in a coordinate
        Coordinate location  = new Coordinate(round.getLocation().getLatitude() , round.getLocation().getLongitude());
        Coordinate guess = new Coordinate(request.guessLatitude() , request.guessLongitude());

        //calculate score
        ScoreResult score = scoringService.calculateScore(location, guess);

        //save guess for round to db
        round.setGuessLat(request.guessLatitude());
        round.setGuessLng(request.guessLongitude());
        round.setAnsweredAt(LocalDateTime.now());
        round.setDistanceMeters(score.distanceMeters());
        round.setScore(score.score());

        roundRepository.save(round);


       //check if game is complete

        List<Round> allRounds = roundRepository.findByGame(game);
        boolean completed = allRounds.stream().noneMatch(r -> r.getAnsweredAt() == null);

        //sum up score for all rounds
        int totalScore = allRounds.stream()
                .filter(r -> r.getScore() != null)
                .mapToInt(Round::getScore)
                .sum();

        if(completed) {
            game.setStatus(GameStatus.COMPLETED);
            game.setTotalScore(totalScore);

            if(game.getTotalScore() > user.getHighScore()) {
                user.setHighScore(game.getTotalScore());
            }

            int gamesPlayed = user.getGamesPlayed();

            user.setGamesPlayed(gamesPlayed + 1);

            userRepository.save(user);

            gameRepository.save(game);

        }



        return  new GuessResponse(score.score() , score.distanceMeters(), round.getLocation().getName() , round.getLocation().getCity() , round.getLocation().getProvince() , round.getLocation().getLatitude() , round.getLocation().getLongitude(), completed, totalScore);



    }

}
