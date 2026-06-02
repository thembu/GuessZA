package com.thembu.guessza.round;


import com.thembu.guessza.game.Game;
import com.thembu.guessza.location.Location;
import com.thembu.guessza.location.LocationRepository;
import com.thembu.guessza.user.User;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.useRepresentation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@DataJpaTest
//dont use in memory h2 but use database from data source
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
 class RoundRepositoryTest {

    @Autowired
    private  TestEntityManager entityManager;

    @Autowired
    private  RoundRepository roundRepository;
    @Autowired
    private LocationRepository locationRepository;

    @Test
    void emptyListWhenNoRoundsFound() {

        User user = makeUser("thembu");
        //create the game
        Game game = makeGame(user);

        //call method

        List<Round> rounds = roundRepository.findByGame(game);

        //check if empty list is returned for game

        assertThat(rounds).isEmpty();

    }

    @Test
    void allRoundsForGame(){
        User user = makeUser("thembu");
        Game game = makeGame(user);

        //create rounds
        Round r1 = makeRound(game , anyLocation(),1,null);
        Round r2 = makeRound(game , anyLocation(),2,null);
        Round r3 = makeRound(game , anyLocation(),3,null);

        //call method

        List<Round> rounds = roundRepository.findByGame(game);

        //check that id in rounds list matches that in round objects

        assertThat(rounds).extracting(Round::getId).containsExactlyInAnyOrder(r1.getId(), r2.getId(), r3.getId());


    }

    @Test
    void ExcludeOtherGameRounds(){
        User user = makeUser("thembu");
        Game game1 = makeGame(user);
        Game game2 = makeGame(user);

        //game1 rounds

        Round r1 = makeRound(game1 , anyLocation(), 1, null);
        Round r2 = makeRound(game1, anyLocation(),2, null);

        //game2 rounds

        Round r3 = makeRound(game2 , anyLocation(), 1 , null);
        Round r4 = makeRound(game2 , anyLocation() , 2 , null);

        //call method

        List<Round> rounds = roundRepository.findByGame(game1);

        assertThat(rounds).extracting(Round::getId).containsExactlyInAnyOrder(r1.getId(), r2.getId());
    }

    @Test
    void returnsEmptyWhenAllRoundsAnswered () {

        User user = makeUser("thembu");
        Game game = makeGame(user);

        //create rounds
        Round r1 = makeRound(game , anyLocation(),1,LocalDateTime.now());
        Round r2 = makeRound(game , anyLocation(),2,LocalDateTime.now());

        //call method

        Optional<Round> round = roundRepository.findFirstByGameAndAnsweredAtIsNullOrderByRoundNumberAsc(game);

        //check if its empty

        assertThat(round).isEmpty();

    }

    @Test
    void returnLowestRoundNumberWhenMultipleUnanswered() {
        User user = makeUser("thembu");
        Game game = makeGame(user);

        //create rounds
        Round r1 = makeRound(game , anyLocation(),2,null);
        Round r2 = makeRound(game , anyLocation(),3,null);
        Round r3 = makeRound(game , anyLocation(),1,null);

        //call method

        Optional<Round> round = roundRepository.findFirstByGameAndAnsweredAtIsNullOrderByRoundNumberAsc(game);

        //check if it exists and is lowest number

        assertThat(round).isPresent();
        assertThat(round.get().getRoundNumber()).isEqualTo(1);

    }


    @Test
    void excludeAlreadyAnsweredRounds() {

        User user = makeUser("thembu");
        Game game = makeGame(user);

        //create rounds
        Round r1 = makeRound(game , anyLocation(),1,LocalDateTime.now());
        Round r2 = makeRound(game , anyLocation(),2,null);

        //call method

        Optional<Round> round = roundRepository.findFirstByGameAndAnsweredAtIsNullOrderByRoundNumberAsc(game);

        assertThat(round).isPresent();
        assertThat(round.get().getRoundNumber()).isEqualTo(2);

    }

    @Test
    void findFirstExcludesOtherGameRounds () {

        User user = makeUser("thembu");
        Game game1 = makeGame(user);
        Game game2 = makeGame(user);

        //game1 rounds

        Round r1 = makeRound(game1 , anyLocation(), 1, null);
        Round r2 = makeRound(game1, anyLocation(),2, null);

        //game2 rounds

        Round r3 = makeRound(game2 , anyLocation(), 1 , null);
        Round r4 = makeRound(game2 , anyLocation() , 2 , null);

        //call method

        Optional<Round> round = roundRepository.findFirstByGameAndAnsweredAtIsNullOrderByRoundNumberAsc(game1);

        assertThat(round).isPresent();
        assertThat(round.get().getId()).isEqualTo(r1.getId());

    }




    private User makeUser(String nickname) {
        User u = new User();
        u.setNickname(nickname);
        entityManager.persist(u);
        return u;
    }


    private Game makeGame(User user) {
        Game g = new Game();
        g.setUser(user);
        entityManager.persist(g);
        return g;
    }

    private Location anyLocation() {
        return locationRepository.findAll().getFirst();
    }

    private Round makeRound(Game game, Location location, int roundNumber, LocalDateTime answeredAt) {
        Round r = new Round();
        r.setGame(game);
        r.setLocation(location);
        r.setRoundNumber(roundNumber);
        r.setAnsweredAt(answeredAt);   // null if unanswered
        entityManager.persist(r);
        return r;
    }




}
