package com.thembu.guessza.game.dto;

import com.thembu.guessza.location.Province;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateProvinceGame(

        @NotNull(message = "must not be null")
        UUID userId,

        @NotNull(message = "must not be null")
        Province province

) {



}
