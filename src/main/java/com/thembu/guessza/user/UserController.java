package com.thembu.guessza.user;

import com.thembu.guessza.user.dto.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class UserController {

    private  final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/me")
    public UserResponse getUser(@AuthenticationPrincipal UserDetails principal) {
        return userService.getUser(principal.getUsername());
    }

}
