package com.thembu.guessza.challenge.dto;

import java.util.List;

public record PlayerResult(String nickname, Integer totalScore, List<RoundResult> rounds) {}