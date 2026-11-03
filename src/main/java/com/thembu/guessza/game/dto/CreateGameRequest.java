package com.thembu.guessza.game.dto;


import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateGameRequest(

        @NotNull(message = "must not be null")
        UUID userId,
        String province
) {

}



