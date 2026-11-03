package com.thembu.guessza.game.dto;


import com.thembu.guessza.location.Province;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateGameRequest(

        @NotNull
        UUID userId
) {

}



