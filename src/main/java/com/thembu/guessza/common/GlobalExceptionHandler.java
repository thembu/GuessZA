package com.thembu.guessza.common;

import com.thembu.guessza.game.GameNotFoundException;
import com.thembu.guessza.location.InsufficientLocationsException;
import com.thembu.guessza.round.RoundAlreadyAnswered;
import com.thembu.guessza.round.RoundFromCompletedGame;
import com.thembu.guessza.round.RoundNotFoundException;
import com.thembu.guessza.user.UserNotFoundException;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleUserNotFound(UserNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(RoundAlreadyAnswered.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleRoundAlreadyAnswered(RoundAlreadyAnswered ex){return  ex.getMessage();}

    @ExceptionHandler(GameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleGameNotFound(GameNotFoundException ex) {return ex.getMessage();}


    @ExceptionHandler(RoundNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public  String handleRoundNotFound(RoundNotFoundException ex){return ex.getMessage();}

    @ExceptionHandler(RoundFromCompletedGame.class)
    @ResponseStatus(HttpStatus.GONE)
    public  String handleRoundNotFound(RoundFromCompletedGame ex){return ex.getMessage();}


}
