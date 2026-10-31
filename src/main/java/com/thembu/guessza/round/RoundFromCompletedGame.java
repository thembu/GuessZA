package com.thembu.guessza.round;

import java.util.UUID;

public class RoundFromCompletedGame extends RuntimeException {
    public RoundFromCompletedGame(UUID id) {
        super("Game " + id + " has already been completed");
    }
}