package com.thembu.guessza.round;

import com.thembu.guessza.game.Game;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.UUID;

public interface RoundRepository extends JpaRepository<Round, UUID> {

    List<Round> findByGame(Game game);

}
