package com.thembu.guessza.round.dto;

import jakarta.validation.constraints.*;

public record GuessRequest(

        @NotNull @DecimalMin("-90.0")  @DecimalMax("90.0")
        double guessLatitude ,
        @NotNull @DecimalMin("-180.0") @DecimalMax("180.0")
        double guessLongitude
) {
}
