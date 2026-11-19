package com.thembu.guessza.challenge;

import java.util.UUID;

public class ChallengeNotFoundException extends RuntimeException {
    public ChallengeNotFoundException(String code) {
        super("challenge " + code + " not found");
    }
}
