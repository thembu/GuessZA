package com.thembu.guessza.challenge.dto;

import java.time.LocalDate;
import java.util.List;

// date is the SAST calendar day the board is for; entries is empty if no daily exists yet or nobody has finished
public record DailyLeaderboardResponse(LocalDate date, List<LeaderboardEntry> entries) {}
