package com.thembu.guessza.user;

import com.thembu.guessza.user.dto.CreateUserRequest;
import com.thembu.guessza.user.dto.UserResponse;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class UserService {

    private  final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserResponse createUser (CreateUserRequest request) {

        //map dto -> entity
        User user = new  User();
        user.setNickname(request.nickname());

        //save to db

        User saved = userRepository.save(user);

        return  new UserResponse(saved.getId() , saved.getNickname(), saved.getGamesPlayed(), saved.getHighScore());

    }

    public UserResponse getUser(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        return  new UserResponse(user.getId() , user.getNickname(), user.getGamesPlayed(), user.getHighScore());

    }

}
