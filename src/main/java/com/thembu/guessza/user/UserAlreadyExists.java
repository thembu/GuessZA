package com.thembu.guessza.user;

public class UserAlreadyExists extends RuntimeException {
    public UserAlreadyExists(String username) {

        super("user " + username + "already exists");
    }
}
