package com.thembu.guessza.challenge.dto;

import com.thembu.guessza.game.GameStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

// myGameId lets the page resume an IN_PROGRESS game from any device.
// results and locations are empty until the caller has COMPLETED the challenge.
public record ChallengeInfoResponse(String code, String creator, LocalDateTime expiresAt,
                                    boolean expired, GameStatus myStatus, UUID myGameId,
                                    List<PlayerResult> results, List<RoundLocation> locations) {}
