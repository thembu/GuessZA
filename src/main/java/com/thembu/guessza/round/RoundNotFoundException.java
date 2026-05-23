package com.thembu.guessza.round;

import java.util.UUID;

public class RoundNotFoundException extends RuntimeException {
    public RoundNotFoundException(UUID id) {
        super("Round " + id +" Not Found");
    }
}
