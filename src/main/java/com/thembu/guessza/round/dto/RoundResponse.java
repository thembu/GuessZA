package com.thembu.guessza.round.dto;

import java.util.UUID;

public record RoundResponse(
        UUID roundId,
        int roundNumber,
        Double latitude,
        Double longitude
) {


}
