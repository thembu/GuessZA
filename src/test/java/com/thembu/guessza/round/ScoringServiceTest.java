package com.thembu.guessza.round;

import com.thembu.guessza.round.dto.Coordinate;
import com.thembu.guessza.round.dto.ScoreResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

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
    void scoreNeverNegative() {

        //coordinate for  Nkoninga Rd
        Coordinate point = new Coordinate(-28.738782205636063,32.07325776264318);

        //middle of the mark guess

        Coordinate guess = new Coordinate(-27.839460599717334, 32.07325776264318);

        //call method

        ScoreResult result = scoringService.calculateScore(point , guess);

        assertThat(result.score()).isNotNegative();
    }


    @Test
     void scoreNeverGreaterThanFiveThousand(){

        //coordinate for  Nkoninga Rd
        Coordinate point = new Coordinate(-28.738782205636063,32.07325776264318);

        Coordinate guess = new Coordinate(-28.74, 32.10);

        //Call method

        ScoreResult result = scoringService.calculateScore(point, guess);

        //check if contract holds

        assertThat(result.score()).isLessThanOrEqualTo(5000);

    }


    @Test
     void badGuessScoresZero() {

        //coordinate for  Nkoninga Rd
        Coordinate point = new Coordinate(-28.738782205636063,32.07325776264318);

        //bad guess

        Coordinate antipodePoint = new Coordinate(28.738782205636063, -147.92674223735682);

        //call method

        ScoreResult result = scoringService.calculateScore(point , antipodePoint);

        assertThat(result.score()).isZero();

    }

    @Test
     void distanceCloseToRealWorld() {

        //Coordinates for Cape Town

        Coordinate point = new Coordinate(-33.9249, 18.4241);

        //Coordinates for Johannesburg

        Coordinate guess = new Coordinate(-26.2041,28.0475);

        //call method

        ScoreResult result = scoringService.calculateScore(point , guess);

        //actual distance

        int actualDistance = 1270000;

        //distance is within 25km tolerance

        assertThat(result.distanceMeters()).isCloseTo(actualDistance,within(25000));


    }


    @Test
    void distanceIsSymmetricInArguments() {

        //coordinate for  Nkoninga Rd
        Coordinate point = new Coordinate(-28.738782205636063,32.07325776264318);

        Coordinate guess = new Coordinate(-28.74, 32.10);

        //call method

        ScoreResult firstResult = scoringService.calculateScore(point, guess);

        ScoreResult secondResult = scoringService.calculateScore(guess , point);

        assertThat(firstResult.distanceMeters()).isEqualTo(secondResult.distanceMeters());


    }


    @ParameterizedTest
    @CsvSource({
            "10000,   500000",
            "1000,    100000",
            "50000,   2000000"
    })

    void closerGuessScoresAtLeastAsFarGuess(int nearMeters , int farMeters) {

        //actual point
        Coordinate point = new Coordinate(-26.20, 28.05);

        //near guess
        Coordinate near   = new Coordinate(point.latitude() + nearMeters / 111000.0, point.longitude());

        //far guess
        Coordinate far    = new Coordinate(point.latitude() + farMeters  / 111000.0, point.longitude());

        //call method

        ScoreResult firstResult = scoringService.calculateScore(point , near);
        ScoreResult secondResult = scoringService.calculateScore(point,far);

        assertThat(firstResult.score()).isGreaterThanOrEqualTo(secondResult.score());

    }





}
