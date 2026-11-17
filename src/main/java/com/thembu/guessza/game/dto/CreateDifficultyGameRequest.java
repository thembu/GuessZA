package com.thembu.guessza.game.dto;


import com.thembu.guessza.location.Difficulty;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateDifficultyGameRequest(

        @NotNull
        Difficulty difficulty


) {

}



