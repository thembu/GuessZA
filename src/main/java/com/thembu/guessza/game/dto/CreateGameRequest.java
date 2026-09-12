package com.thembu.guessza.game.dto;

import com.thembu.guessza.game.GameStatus;
import com.thembu.guessza.location.Province;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record CreateGameRequest(
        UUID userId,
        String province
) {

}



