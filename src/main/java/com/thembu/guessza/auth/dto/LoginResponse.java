package com.thembu.guessza.auth.dto;

public record LoginResponse(
        String token,
        String tokenType,
        String message
) {
}
