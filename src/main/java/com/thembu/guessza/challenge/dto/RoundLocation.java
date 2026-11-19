package com.thembu.guessza.challenge.dto;

import com.thembu.guessza.location.Province;

// The actual location of one challenge round. Only sent to players who have finished,
// otherwise it would give the answers away.
public record RoundLocation(Integer roundNumber, String name, String city, Province province,
                            double latitude, double longitude) {}
