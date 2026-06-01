package com.thembu.guessza.round;

import com.thembu.guessza.round.dto.Coordinate;
import com.thembu.guessza.round.dto.ScoreResult;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

 class ScoringServiceTest {


    private final ScoringService scoringService = new  ScoringService();

    @Test
     void exactGuessScoresFiveThousand() {
        //coordinate for  Nkoninga Rd
        Coordinate point = new Coordinate(-28.738782205636063,32.07325776264318);

        //Call method

        ScoreResult result = scoringService.calculateScore(point, point);

        //check if contract holds

        assertThat(result.score()).isEqualTo(5000);

    }

    @Test
    void ScoreNeverNegative() {

        //coordinate for  Nkoninga Rd
        Coordinate point = new Coordinate(-28.738782205636063,32.07325776264318);

        //middle of the mark guess

        Coordinate guess = new Coordinate(-27.839460599717334, 32.07325776264318);

        //call method

        ScoreResult result = scoringService.calculateScore(point , guess);

        assertThat(result.score()).isNotNegative();
    }


    @Test
     void ScoreNeverGreaterThanFiveThousand(){

        //coordinate for  Nkoninga Rd
        Coordinate point = new Coordinate(-28.738782205636063,32.07325776264318);

        Coordinate guess = new Coordinate(-28.74, 32.10);

        //Call method

        ScoreResult result = scoringService.calculateScore(point, guess);

        //check if contract holds

        assertThat(result.score()).isLessThanOrEqualTo(5000);

    }


    @Test
     void BadGuessScoresZero() {

        //coordinate for  Nkoninga Rd
        Coordinate point = new Coordinate(-28.738782205636063,32.07325776264318);

        //bad guess

        Coordinate guess = new Coordinate(0, 0);

        //call method

        ScoreResult result = scoringService.calculateScore(point , guess);

        assertThat(result.score()).isZero();

    }




}
