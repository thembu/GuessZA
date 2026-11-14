package com.thembu.guessza.round;

import java.util.UUID;

public class RoundNotYoursException extends RuntimeException {
    public RoundNotYoursException(UUID id) {
        super( id + "belongs to another player");
    }
}
