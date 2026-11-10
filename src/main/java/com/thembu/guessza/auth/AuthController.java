package com.thembu.guessza.auth;

import com.thembu.guessza.auth.dto.LoginRequest;
import com.thembu.guessza.auth.dto.LoginResponse;
import com.thembu.guessza.config.JwtUtil;
import com.thembu.guessza.user.UserService;
import com.thembu.guessza.user.dto.CreateUserRequest;
import com.thembu.guessza.user.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, AuthenticationManager authManager, JwtUtil jwtUtil) {
        this.userService = userService;
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public UserResponse createUser(@RequestBody @Valid CreateUserRequest request) {
        return  userService.createUser(request);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public LoginResponse login(@RequestBody @Valid LoginRequest request) {
        Authentication auth  = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username() , request.password())
        );

        UserDetails user = (UserDetails) auth.getPrincipal();

        String token = jwtUtil.generateToken(user.getUsername());

        return new LoginResponse(token , "Bearer", "Logged in Successfully ");

    }


}
