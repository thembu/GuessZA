package com.thembu.guessza.challenge.dto;

// one line on the daily board. Scores only, no guesses: anyone can see the board,
// including players who haven't played yet, and guess coordinates would give the answers away
public record LeaderboardEntry(long rank, String nickname, Integer totalScore) {}
