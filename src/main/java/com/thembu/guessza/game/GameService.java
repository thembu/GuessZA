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
import org.hibernate.sql.ast.tree.expression.Star;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private  final UserRepository userRepository;
    private  final RoundRepository roundRepository;
    private  final LocationRepository locationRepository;
    private final  LocationService locationService;

    public GameService(GameRepository gameRepository, UserRepository userRepository, RoundRepository roundRepository, LocationRepository locationRepository, LocationService locationService) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.roundRepository = roundRepository;
        this.locationRepository = locationRepository;
        this.locationService = locationService;
    }


    private GameResponse StartGame(UUID userId, Game game, List<Location> locations ) {


        if(locations.size() < 5){
            locationRepository.deleteVisited(userId);
            return GameResponse.locationsExhausted(userId , true);
        }


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
    public GameResponse createGame(CreateGameRequest request) {

        //create new instance of game entity, add user/player for that game and save it
        Game game = new Game();

        User user = userRepository.findById(request.userId()).orElseThrow(()-> new UserNotFoundException(request.userId()));

        game.setUser(user);

        return StartGame( request.userId(), game , locationService.getRandomLocations(5, request.userId()));

    }


    @Transactional
    public  GameResponse createProvinceGame(CreateProvinceGameRequest request) {
        Game game = new Game();

        User user = userRepository.findById(request.userId()).orElseThrow(()-> new UserNotFoundException(request.userId()));

        game.setUser(user);

        return StartGame( request.userId(), game , locationService.getLocationByProvince(request.province(),5, request.userId()));

    }

}
