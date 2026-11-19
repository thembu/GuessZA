package com.thembu.guessza.challenge;

public class ChallengeAlreadyPlayedException extends RuntimeException {
    public ChallengeAlreadyPlayedException(String code) {
        super("challenge " + code + " already played");
    }
}
