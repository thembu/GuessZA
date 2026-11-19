package com.thembu.guessza.challenge;

import com.thembu.guessza.challenge.dto.ChallengeResponse;
import com.thembu.guessza.game.Game;
import com.thembu.guessza.game.GameService;
import com.thembu.guessza.location.Location;
import com.thembu.guessza.location.LocationRepository;
import com.thembu.guessza.location.LocationService;
import com.thembu.guessza.user.User;
import com.thembu.guessza.user.UserNotFoundException;
import com.thembu.guessza.user.UserRepository;
import com.thembu.guessza.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final LocationService locationService;
    private final GameService gameService;
    private  final UserRepository userRepository;
    private final LocationRepository locationRepository;

    public ChallengeService(ChallengeRepository challengeRepository, LocationService locationService, GameService gameService, UserRepository userRepository, LocationRepository locationRepository) {
        this.challengeRepository = challengeRepository;
        this.locationService = locationService;
        this.gameService = gameService;
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
    }

    @Transactional
    public ChallengeResponse  createChallenge(String username) {

        //create the actual challenge first before connecting to a game

        Challenges challenge = new Challenges();

        //set authed user at the creator
        User creator = userRepository.findUserByUsername(username).orElseThrow(() -> new UserNotFoundException(username));

        challenge.setCreator(creator);

        //generate the code
        challenge.setCode(CodeGenerator.generate(16));

        //set expiry date
        challenge.setExpires_at(LocalDateTime.now().plusHours(10));

        //save challenge
        challengeRepository.save(challenge);

        List<Location> locations = locationService.getRandomLocations(5 , creator.getId());

        int round = 1;

        for(Location location : locations) {

            locationRepository.saveChallenge(challenge.getId() , round , location.getId());
            round++;
        }


        return new ChallengeResponse(challenge.getCode() , "/challenge.html?code=".concat(challenge.getCode()));

    }

}
