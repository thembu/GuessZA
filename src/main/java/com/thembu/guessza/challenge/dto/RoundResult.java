package com.thembu.guessza.challenge.dto;

// guessLat/guessLng let the results map pin each player's guess
public record RoundResult(Integer roundNumber, Integer score, Integer distanceMeters,
                          Double guessLat, Double guessLng) {}
