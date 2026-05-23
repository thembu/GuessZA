package com.thembu.guessza.round.dto;

import com.thembu.guessza.location.Province;

public record GuessResponse(
        int score,
        int distanceMeters,
        String locationName,
        String locationCity,
        Province locationProvince,
        double locationLatitude,
        double locationLongitude,
        boolean gameOver,
        int totalScore

) {
}
