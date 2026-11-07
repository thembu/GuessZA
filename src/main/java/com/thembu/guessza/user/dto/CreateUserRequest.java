package com.thembu.guessza.user.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
        @NotBlank
        @Size(max = 20)
        String nickname,

        @NotBlank
        @Size(min = 3 ,  max = 20)
        String username,

        @Size(min = 8)
        String password
) {

}
