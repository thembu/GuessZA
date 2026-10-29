package com.thembu.guessza.game.dto;

import java.util.UUID;

public record CreateGameRequest(
        UUID userId,
        String province
) {

}



