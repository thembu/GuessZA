package com.thembu.guessza.game.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.thembu.guessza.game.GameStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record GameResponse(UUID id , UUID userId, boolean locationsExhausted, GameStatus status, LocalDateTime createdAt) {

    //custom constructor for when we run out of locations
    public static   GameResponse locationsExhausted(UUID userId ,  boolean locationsExhausted) {
        return  new GameResponse(null , userId , locationsExhausted , null , null);
    }


}
