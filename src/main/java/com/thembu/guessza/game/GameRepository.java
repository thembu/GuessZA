package com.thembu.guessza.game;

import com.thembu.guessza.challenge.ChallengeResultRow;
import com.thembu.guessza.challenge.Challenges;
import com.thembu.guessza.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GameRepository extends JpaRepository<Game , UUID> {

    Optional<Game> findByChallengeAndUser(Challenges challenge, User user);

    @Query(value = """
          SELECT u.nickname        AS "nickname",
                 g.id              AS "gameId",
                 g.total_score     AS "totalScore",
                 r.round_number    AS "roundNumber",
                 r.score           AS "score",
                 r.distance_meters AS "distanceMeters",
                 r.guess_lat       AS "guessLat",
                 r.guess_lng       AS "guessLng"
          FROM games g
          JOIN users  u ON u.id = g.user_id
          JOIN rounds r ON r.game_id = g.id
          WHERE g.challenge_id = :challengeId
            AND g.status = 'COMPLETED'
          ORDER BY g.total_score DESC, g.id, r.round_number
          """, nativeQuery = true)

    List<ChallengeResultRow> findChallengeResults(@Param("challengeId") UUID challengeId);


}
