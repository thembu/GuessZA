package com.thembu.guessza.game;

import com.thembu.guessza.game.dto.CreateGameRequest;
import com.thembu.guessza.game.dto.CreateProvinceGameRequest;
import com.thembu.guessza.game.dto.GameResponse;
import com.thembu.guessza.location.Location;
import com.thembu.guessza.location.LocationRepository;
import com.thembu.guessza.location.LocationService;
import com.thembu.guessza.round.Round;
import com.thembu.guessza.round.RoundRepository;
import com.thembu.guessza.user.User;
import com.thembu.guessza.user.UserNotFoundException;
import com.thembu.guessza.user.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private  final UserRepository userRepository;
    private  final RoundRepository roundRepository;
    private  final LocationRepository locationRepository;
    private final  LocationService locationService;
    private static final int ROUNDS_PER_GAME = 5;

    public GameService(GameRepository gameRepository, UserRepository userRepository, RoundRepository roundRepository, LocationRepository locationRepository, LocationService locationService) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.roundRepository = roundRepository;
        this.locationRepository = locationRepository;
        this.locationService = locationService;
    }


    private GameResponse startGame(UUID userId, Game game, List<Location> locations ) {


        if(locations.size() < ROUNDS_PER_GAME){
            locationRepository.deleteVisited(userId);
            return GameResponse.locationsExhausted(userId , true);
        }


        game.setTotalRounds(ROUNDS_PER_GAME);
        //after check for locations save game
         gameRepository.save(game);


        int  count = 1;
        for (Location location : locations) {

            Round round = new Round();
            round.setGame(game);
            round.setLocation(location);
            round.setRoundNumber(count);
            count += 1;
            roundRepository.save(round);
        }

        //return game response to  user
        return  new GameResponse(game.getId(), userId,false,GameStatus.IN_PROGRESS, game.getCreatedAt());



    }

    @Transactional
    public GameResponse createGame(String username) {

        //create new instance of game entity, add user/player for that game and save it
        Game game = new Game();

        User user = userRepository.findUserByUsername(username).orElseThrow(()-> new UserNotFoundException(username));

        game.setUser(user);

        UUID userId = user.getId();

        return startGame( userId, game , locationService.getRandomLocations(ROUNDS_PER_GAME, userId));

    }


    @Transactional
    public  GameResponse createProvinceGame(CreateProvinceGameRequest request ,  String username) {
        Game game = new Game();

        User user = userRepository.findUserByUsername(username).orElseThrow(()-> new UserNotFoundException(username));

        game.setUser(user);

        UUID userId = user.getId();

        return startGame( userId, game , locationService.getLocationByProvince(request.province(),ROUNDS_PER_GAME, userId));

    }

}
