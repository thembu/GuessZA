package com.thembu.guessza.round;

import com.thembu.guessza.game.Game;
import com.thembu.guessza.game.GameNotFoundException;
import com.thembu.guessza.game.GameRepository;
import com.thembu.guessza.location.Location;
import com.thembu.guessza.location.LocationRepository;
import com.thembu.guessza.round.dto.RoundResponse;
import com.thembu.guessza.user.UserRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

//plain unit test: no Spring, the service is built by hand with mocked repositories
class RoundServiceTest {

    private final GameRepository gameRepository = mock(GameRepository.class);
    private final RoundRepository roundRepository = mock(RoundRepository.class);

    private final RoundService roundService = new RoundService(
            gameRepository,
            roundRepository,
            mock(ScoringService.class),
            mock(UserRepository.class),
            mock(LocationRepository.class));

    private final UUID gameId = UUID.randomUUID();
    private final Game game = new Game();

    @Test
    void currentRoundOfUnknownGameThrowsGameNotFound() {
        when(gameRepository.findById(gameId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> roundService.getCurrentRound(gameId))
                .isInstanceOf(GameNotFoundException.class);
    }

    @Test
    void currentRoundOfCompletedGameThrowsRoundFromCompletedGame() {
        //build rounds before stubbing: round() stubs a Location mock, and Mockito can't nest stubbing
        List<Round> rounds = List.of(round(1, true), round(2, true));

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        when(roundRepository.findByGame(game)).thenReturn(rounds);

        assertThatThrownBy(() -> roundService.getCurrentRound(gameId))
                .isInstanceOf(RoundFromCompletedGame.class);
    }

    @Test
    void currentRoundOfGameInProgressReturnsFirstUnansweredRound() {
        Round unanswered = round(2, false);
        List<Round> rounds = List.of(round(1, true), unanswered);

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        when(roundRepository.findByGame(game)).thenReturn(rounds);
        when(roundRepository.findFirstByGameAndAnsweredAtIsNullOrderByRoundNumberAsc(game))
                .thenReturn(Optional.of(unanswered));

        RoundResponse response = roundService.getCurrentRound(gameId);

        assertThat(response.roundNumber()).isEqualTo(2);
        assertThat(response.latitude()).isEqualTo(-26.2);
        assertThat(response.longitude()).isEqualTo(28.05);
    }

    private Round round(int roundNumber, boolean answered) {
        //Location has no setters, so mock the coordinates instead
        Location location = mock(Location.class);
        when(location.getLatitude()).thenReturn(-26.2);
        when(location.getLongitude()).thenReturn(28.05);

        Round round = new Round();
        round.setGame(game);
        round.setLocation(location);
        round.setRoundNumber(roundNumber);
        round.setAnsweredAt(answered ? LocalDateTime.now() : null);
        return round;
    }
}
