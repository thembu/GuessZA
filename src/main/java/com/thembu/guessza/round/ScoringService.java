package com.thembu.guessza.round;


import com.thembu.guessza.round.dto.Coordinate;
import com.thembu.guessza.round.dto.ScoreResult;
import org.springframework.stereotype.Service;

@Service
public class ScoringService {

    private static final double EARTH_RADIUS = 6371.0;

    public ScoreResult calculateScore(Coordinate actual , Coordinate guess) {

        // Convert degrees to radians
        double phi1 = Math.toRadians(actual.latitude());
        double phi2 = Math.toRadians(guess.latitude());
        double deltaPhi = Math.toRadians(guess.latitude()) - Math.toRadians(actual.latitude());
        double deltaLambda = Math.toRadians(guess.longitude()) - Math.toRadians(actual.longitude());

        // Apply Haversine formula
        double a = Math.sin(deltaPhi / 2) * Math.sin(deltaPhi / 2) +
                Math.cos(phi1) * Math.cos(phi2) *
                        Math.sin(deltaLambda / 2) * Math.sin(deltaLambda / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        int distanceMeters = (int) (EARTH_RADIUS * c * 1000);

        int score = (int) (5000 * Math.exp(-distanceMeters / 150_000.0));

        return  new ScoreResult(distanceMeters , score);


    }



}
