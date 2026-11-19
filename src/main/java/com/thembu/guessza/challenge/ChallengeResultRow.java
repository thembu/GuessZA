package com.thembu.guessza.challenge;

import java.util.UUID;

public interface ChallengeResultRow {
    String getNickname();
    UUID getGameId();
    Integer getTotalScore();
    Integer getRoundNumber();
    Integer getScore();
    Integer getDistanceMeters();
}
