package com.thembu.guessza.user;

import com.thembu.guessza.user.dto.CreateUserRequest;
import com.thembu.guessza.user.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {

    private  final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public UserResponse createUser(@RequestBody @Valid CreateUserRequest request) {
        return  userService.createUser(request);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/me")
    public UserResponse getUser(@RequestBody UUID id) {
        return userService.getUser(id);
    }

}
