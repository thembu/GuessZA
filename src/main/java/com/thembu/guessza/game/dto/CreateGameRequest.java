package com.thembu.guessza.game.dto;

import com.thembu.guessza.game.GameStatus;
import com.thembu.guessza.location.Province;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record CreateGameRequest(
        UUID userId,
        String province,
        List<String> visitedLocations
) {

    public CreateGameRequest {
        //if visite locations is null replace with empty list otherwise leave as is(null check)
        visitedLocations = visitedLocations == null ? List.of() : visitedLocations;
    }

}
