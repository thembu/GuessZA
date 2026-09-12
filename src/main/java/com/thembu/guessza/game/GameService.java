package com.thembu.guessza.game;

import com.thembu.guessza.game.dto.CreateGameRequest;
import com.thembu.guessza.game.dto.GameResponse;
import com.thembu.guessza.location.Location;
import com.thembu.guessza.location.LocationRepository;
import com.thembu.guessza.location.LocationService;
import com.thembu.guessza.round.Round;
import com.thembu.guessza.round.RoundRepository;
import com.thembu.guessza.user.User;
import com.thembu.guessza.user.UserNotFoundException;
import com.thembu.guessza.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Transactional
    public GameResponse createGame(CreateGameRequest request, String type) {

        //create new instance of game entity, add user/player for that game and save it
        Game game = new Game();

        User user = userRepository.findById(request.userId()).orElseThrow(()-> new UserNotFoundException(request.userId()));

        game.setUser(user);

        //generate 5 random locations and save them in 5 separate rounds of the game

        List<Location> locations =  new ArrayList<>();

        if(type.equals("Standard")) {

            locations = locationService.getRandomLocations(5);

            if(locations.size() < 5){
                locationRepository.deleteVisited(user.getId());
                return GameResponse.locationsExhausted(user.getId() , true);
            }

        } else if (type.equals("Province")){

            locations = locationService.getLocationByProvince(request.province() , 5 );
            if(locations.size() < 5) {
                locationRepository.deleteVisited(user.getId());
                return GameResponse.locationsExhausted(user.getId() , true);

            }

        }

        //after check for locations save game
        Game savedGame = gameRepository.save(game);


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
       return  new GameResponse(savedGame.getId(), user.getId(),false,GameStatus.IN_PROGRESS, LocalDateTime.now());

    }

}
