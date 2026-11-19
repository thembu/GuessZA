package com.thembu.guessza.challenge.dto;

import jakarta.validation.constraints.NotBlank;

public record ChallengeResponse(

        @NotBlank
        String code,

        @NotBlank
        String link
) {
}
