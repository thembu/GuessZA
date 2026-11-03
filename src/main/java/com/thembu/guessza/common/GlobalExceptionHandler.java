package com.thembu.guessza.common;

import com.thembu.guessza.game.GameNotFoundException;
import com.thembu.guessza.location.InsufficientLocationsException;
import com.thembu.guessza.round.RoundAlreadyAnswered;
import com.thembu.guessza.round.RoundFromCompletedGame;
import com.thembu.guessza.round.RoundNotFoundException;
import com.thembu.guessza.user.UserNotFoundException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;

import org.springframework.http.ProblemDetail;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.View;

@RestControllerAdvice
public class GlobalExceptionHandler {


    private final View error;

    public GlobalExceptionHandler(View error) {
        this.error = error;
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ProblemDetail handleUserNotFound(UserNotFoundException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    //intercept MethodArgumentNotValidException so when validation fails correct api response is sent

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationException(MethodArgumentNotValidException ex) {

        //fetch first  validation error message

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getDefaultMessage())
                .findFirst()
                .orElse("Validation failed");



        return  ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST , message);
    }

    @ExceptionHandler(RoundAlreadyAnswered.class)
    public ProblemDetail handleRoundAlreadyAnswered(RoundAlreadyAnswered ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
    }

    //intercepts optimistic locking failure exception and gives out custom error for it used to prevent two round guesses happening at same time
    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ProblemDetail handleVersionCheckFail(OptimisticLockingFailureException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT , "The resource was modified by another request. Refresh and try again.");
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
