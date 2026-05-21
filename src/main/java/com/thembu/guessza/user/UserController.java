package com.thembu.guessza.user;

import com.thembu.guessza.user.dto.CreateUserRequest;
import com.thembu.guessza.user.dto.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

    private  final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public UserResponse createUser(@RequestBody  CreateUserRequest request) {
        return  userService.createUser(request);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable UUID id) {
        return userService.getUser(id);
    }

}
