package com.thembu.guessza.common;

import com.thembu.guessza.game.GameNotFoundException;
import com.thembu.guessza.location.InsufficientLocationsException;
import com.thembu.guessza.round.RoundAlreadyAnswered;
import com.thembu.guessza.round.RoundFromCompletedGame;
import com.thembu.guessza.round.RoundNotFoundException;
import com.thembu.guessza.user.UserNotFoundException;
import org.springframework.http.HttpStatus;

import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ProblemDetail handleUserNotFound(UserNotFoundException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(RoundAlreadyAnswered.class)
    public ProblemDetail handleRoundAlreadyAnswered(RoundAlreadyAnswered ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(GameNotFoundException.class)
    public ProblemDetail handleGameNotFound(GameNotFoundException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(RoundNotFoundException.class)
    public ProblemDetail handleRoundNotFound(RoundNotFoundException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(RoundFromCompletedGame.class)
    public ProblemDetail handleRoundFromCompletedGame(RoundFromCompletedGame ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.GONE, ex.getMessage());
    }



}
