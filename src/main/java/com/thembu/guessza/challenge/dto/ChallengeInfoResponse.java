package com.thembu.guessza.challenge.dto;

import com.thembu.guessza.game.GameStatus;

import java.time.LocalDateTime;
import java.util.List;

public record ChallengeInfoResponse(String code, String creator, LocalDateTime expiresAt,
                                    boolean expired, GameStatus myStatus, List<PlayerResult> results) {}

