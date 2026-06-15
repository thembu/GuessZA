package com.thembu.guessza.game.dto;

import com.thembu.guessza.game.GameStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record CreateGameRequest(
        UUID userId,
        List<String> visitedLocations
) {

    public CreateGameRequest {
        visitedLocations = visitedLocations == null ? List.of() : visitedLocations;
    }

}
