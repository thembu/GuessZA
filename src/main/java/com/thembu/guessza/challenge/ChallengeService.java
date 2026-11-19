package com.thembu.guessza.challenge;

import com.thembu.guessza.challenge.dto.ChallengeInfoResponse;
import com.thembu.guessza.challenge.dto.ChallengeResponse;
import com.thembu.guessza.challenge.dto.PlayerResult;
import com.thembu.guessza.challenge.dto.RoundLocation;
import com.thembu.guessza.challenge.dto.RoundResult;
import com.thembu.guessza.game.Game;
import com.thembu.guessza.game.GameRepository;
import com.thembu.guessza.game.GameService;
import com.thembu.guessza.game.GameStatus;
import com.thembu.guessza.game.dto.GameResponse;
import com.thembu.guessza.location.Location;
import com.thembu.guessza.location.LocationRepository;
import com.thembu.guessza.location.LocationService;
import com.thembu.guessza.user.User;
import com.thembu.guessza.user.UserNotFoundException;
import com.thembu.guessza.user.UserRepository;
import com.thembu.guessza.user.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final LocationService locationService;
    private final GameService gameService;
    private final GameRepository gameRepository;
    private  final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private static final int ROUNDS_PER_GAME = 5;

    public ChallengeService(ChallengeRepository challengeRepository, LocationService locationService, GameService gameService, GameRepository gameRepository, UserRepository userRepository, LocationRepository locationRepository) {
        this.challengeRepository = challengeRepository;
        this.locationService = locationService;
        this.gameService = gameService;
        this.gameRepository = gameRepository;
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


    @Transactional
    public GameResponse StartChallengeGame(String code ,  String username) {
        //create new instance of game entity, add user/player for that game and the challenge they are playing save it
        Game game = new Game();

        //get challenge using the code
        Challenges challenge = challengeRepository.findChallengesByCode(code).orElseThrow(() -> new ChallengeNotFoundException(code));

        //check if challenge has expired yet
        if(LocalDateTime.now().isAfter(challenge.getExpires_at())) throw new ChallengeExpiredException(code);

        game.setChallenge(challenge);

        User user = userRepository.findUserByUsername(username).orElseThrow(()-> new UserNotFoundException(username));

        game.setUser(user);

        UUID userId = user.getId();


        try {
            return gameService.startGame(userId, game, locationRepository.findChallengeLocations(challenge.getId(), ROUNDS_PER_GAME), () -> {
            });
        } catch (DataIntegrityViolationException e) {
            throw new ChallengeAlreadyPlayedException(code);
        }

    }


    @Transactional(readOnly = true)
    public ChallengeInfoResponse getChallengeInfo(String code, String username) {

        // 1. the challenge → 404 if unknown. Expiry is NOT checked: results stay visible after expiry
        Challenges challenge = challengeRepository.findChallengesByCode(code)
                .orElseThrow(() -> new ChallengeNotFoundException(code));

        // 2. who's asking
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        // 3. their game on this challenge, if any (null status = never started).
        //    The id lets the page resume an IN_PROGRESS game from any device.
        Optional<Game> myGame = gameRepository.findByChallengeAndUser(challenge, user);
        GameStatus myStatus = myGame.map(Game::getStatus).orElse(null);
        UUID myGameId = myGame.map(Game::getId).orElse(null);

        // 4. results and actual locations only if they've finished. This is the "don't reveal" rule
        boolean finished = myStatus == GameStatus.COMPLETED;
        List<PlayerResult> results = finished ? buildResults(challenge.getId()) : List.of();
        List<RoundLocation> locations = finished ? buildLocations(challenge.getId()) : List.of();

        return new ChallengeInfoResponse(
                challenge.getCode(),
                challenge.getCreator().getNickname(),
                challenge.getExpires_at(),
                LocalDateTime.now().isAfter(challenge.getExpires_at()),
                myStatus,
                myGameId,
                results,
                locations);
    }

    private List<RoundLocation> buildLocations(UUID challengeId) {
        // findChallengeLocations returns them ORDER BY round_number, and createChallenge numbers
        // rounds 1..n with no gaps, so list position + 1 is the round number
        List<Location> locations = locationRepository.findChallengeLocations(challengeId, ROUNDS_PER_GAME);
        return IntStream.range(0, locations.size())
                .mapToObj(i -> {
                    Location l = locations.get(i);
                    return new RoundLocation(i + 1, l.getName(), l.getCity(), l.getProvince(),
                            l.getLatitude(), l.getLongitude());
                })
                .toList();
    }

    private List<PlayerResult> buildResults(UUID challengeId) {
        // 10 flat rows for 2 players → grouped per game, keeping the score order from SQL
        Map<UUID, List<ChallengeResultRow>> byGame = gameRepository.findChallengeResults(challengeId).stream()
                .collect(Collectors.groupingBy(ChallengeResultRow::getGameId, LinkedHashMap::new, Collectors.toList()));

        return byGame.values().stream()
                .map(rows -> new PlayerResult(
                        rows.get(0).getNickname(),       // the same on every row of one game
                        rows.get(0).getTotalScore(),
                        rows.stream()
                                .map(r -> new RoundResult(r.getRoundNumber(), r.getScore(), r.getDistanceMeters(),
                                        r.getGuessLat(), r.getGuessLng()))
                                .toList()))
                .toList();
    }




}
