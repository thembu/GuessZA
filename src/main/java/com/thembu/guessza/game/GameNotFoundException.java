package com.thembu.guessza.game;

import java.util.UUID;

public class GameNotFoundException extends RuntimeException {
    public GameNotFoundException(UUID id) {
        super("Game " + id + " Not Found");
    }
}
