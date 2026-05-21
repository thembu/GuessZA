package com.thembu.guessza.user.dto;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String nickname,
        Integer gamesPlayed,
        Integer highScore
) {
}
