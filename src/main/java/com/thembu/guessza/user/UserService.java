package com.thembu.guessza.user;

import com.thembu.guessza.user.dto.CreateUserRequest;
import com.thembu.guessza.user.dto.UserResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
public class UserService {

    private  final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public UserService(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Transactional
    public UserResponse createUser (CreateUserRequest request) {

        if(userRepository.existsUserByUsername(request.username())) throw  new UserAlreadyExists(request.username());

        //map dto -> entity
        User user = new  User();
        user.setNickname(request.nickname());
        user.setUsername(request.username());

        //encode password  when creating a user
        user.setPassword(encoder.encode(request.password_hash()));

        //save to db

        User saved = userRepository.save(user);

        return  new UserResponse(saved.getId() , user.getUsername(), saved.getNickname(), saved.getGamesPlayed(), saved.getHighScore());

    }

    @Transactional(readOnly = true)
    public UserResponse getUser(String username) {
        User user = userRepository.findUserByUsername(username).orElseThrow(() -> new UserNotFoundException(username));

        return  new UserResponse(user.getId() , user.getUsername(),user.getNickname(), user.getGamesPlayed(), user.getHighScore());

    }


}
