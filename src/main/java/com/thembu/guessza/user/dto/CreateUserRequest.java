package com.thembu.guessza.user.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateUserRequest(
        @NotBlank
        String nickname
) {

}
