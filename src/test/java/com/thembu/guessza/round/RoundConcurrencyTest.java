package com.thembu.guessza.round;

import com.thembu.guessza.game.Game;
import com.thembu.guessza.game.GameRepository;
import com.thembu.guessza.game.GameStatus;
import com.thembu.guessza.location.LocationRepository;
import com.thembu.guessza.round.dto.GuessRequest;
import com.thembu.guessza.user.User;
import com.thembu.guessza.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

//full Spring context against the real database, with NO test transaction:
//concurrent requests need real, separately committed transactions, so @AfterEach cleans up by hand
@SpringBootTest
class RoundConcurrencyTest {

    private static final int THREADS = 8; //stay under Hikari's default pool of 10

    @Autowired private RoundService roundService;
    @Autowired private RoundRepository roundRepository;
    @Autowired private GameRepository gameRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private LocationRepository locationRepository;

    private User user;
    private Game game;
    private Round round;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setNickname("race-test");
        user = userRepository.save(user);

        game = new Game();
        game.setUser(user);
        game = gameRepository.save(game);

        //a single-round game, so the winning guess also finishes the game
        round = new Round();
        round.setGame(game);
        round.setLocation(locationRepository.findAll().getFirst());
        round.setRoundNumber(1);
        round = roundRepository.save(round);
    }

    @AfterEach
    void tearDown() {
        locationRepository.deleteVisited(user.getId());
        roundRepository.deleteById(round.getId());
        gameRepository.deleteById(game.getId());
        userRepository.deleteById(user.getId());
    }

    @Test
    void staleWriteIsRejectedByVersionCheck() {
        //two independent copies of the same row, both read at version 0
        Round first = roundRepository.findById(round.getId()).orElseThrow();
        Round stale = roundRepository.findById(round.getId()).orElseThrow();

        first.setScore(100);
        roundRepository.save(first); //version 0 -> 1

        stale.setScore(999);
        assertThatThrownBy(() -> roundRepository.save(stale))
                .isInstanceOf(OptimisticLockingFailureException.class);

        assertThat(roundRepository.findById(round.getId()).orElseThrow().getScore()).isEqualTo(100);
    }

    @Test
    void concurrentGuessesOnSameRoundOnlyOneWins() throws Exception {
        CountDownLatch ready = new CountDownLatch(THREADS);
        CountDownLatch go = new CountDownLatch(1);
        AtomicInteger successes = new AtomicInteger();
        List<Throwable> failures = Collections.synchronizedList(new ArrayList<>());

        ExecutorService pool = Executors.newFixedThreadPool(THREADS);
        try {
            for (int i = 0; i < THREADS; i++) {
                pool.submit(() -> {
                    ready.countDown();
                    try {
                        go.await(); //release every thread at the same instant
                        roundService.submitGuess(round.getId(), new GuessRequest(-26.2, 28.05));
                        successes.incrementAndGet();
                    } catch (Throwable t) {
                        failures.add(t);
                    }
                });
            }
            ready.await();
            go.countDown();
            pool.shutdown();
            assertThat(pool.awaitTermination(30, TimeUnit.SECONDS)).isTrue();
        } finally {
            pool.shutdownNow();
        }

        assertThat(successes.get()).isEqualTo(1);
        //losers either saw the round already answered, or lost the version check mid-write
        assertThat(failures).hasSize(THREADS - 1)
                .allSatisfy(t -> assertThat(t).isInstanceOfAny(
                        RoundAlreadyAnswered.class, OptimisticLockingFailureException.class));

        //the game was finished exactly once, so the user's stats were only counted once
        assertThat(userRepository.findById(user.getId()).orElseThrow().getGamesPlayed()).isEqualTo(1);
        assertThat(gameRepository.findById(game.getId()).orElseThrow().getStatus())
                .isEqualTo(GameStatus.COMPLETED);
    }
}
