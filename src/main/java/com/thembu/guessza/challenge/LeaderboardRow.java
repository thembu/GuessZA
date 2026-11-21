package com.thembu.guessza.challenge;

// one leaderboard line from GameRepository.findDailyChallengeResults.
// Getter names must match the quoted column aliases in that query.
public interface LeaderboardRow {
    Long getRank();          // Postgres RANK() returns bigint
    String getNickname();
    Integer getTotalScore();
}
