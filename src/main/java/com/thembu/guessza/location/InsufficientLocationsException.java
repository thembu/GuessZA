package com.thembu.guessza.location;

public class InsufficientLocationsException extends RuntimeException {
    public InsufficientLocationsException(String message) {
        super(message);
    }
}
