package com.thembu.guessza.challenge;

public class ChallengeExpiredException extends RuntimeException {
    public ChallengeExpiredException(String code) {
        super("challenge "+ code + " has expired");
    }
}
