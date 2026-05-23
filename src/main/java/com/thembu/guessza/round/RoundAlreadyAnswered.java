package com.thembu.guessza.round;

import java.util.UUID;

public class RoundAlreadyAnswered extends RuntimeException {
    public RoundAlreadyAnswered(UUID id) {
        super("Round "+ id + " already completed");
    }
}
