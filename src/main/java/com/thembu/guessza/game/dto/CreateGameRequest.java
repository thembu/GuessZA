package com.thembu.guessza.game.dto;

import com.thembu.guessza.game.GameStatus;

import java.util.UUID;

public record CreateGameRequest(
        UUID userId
) {
}
