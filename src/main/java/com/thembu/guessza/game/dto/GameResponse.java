package com.thembu.guessza.game.dto;

import com.thembu.guessza.game.GameStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record GameResponse(UUID id , UUID userId,  GameStatus status, LocalDateTime createdAt) {
}
