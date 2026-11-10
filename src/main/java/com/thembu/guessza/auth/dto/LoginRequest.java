package com.thembu.guessza.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank
        @Size(min = 3 , max = 20)
        String username,

        @NotBlank
        @Size(min = 8)
        String password
) {
}
