package com.thembu.guessza.challenge;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface ChallengeRepository extends JpaRepository<Challenges , UUID> {

    public Optional<Challenges> findChallengesByCode(String code);

    public Optional<Challenges> findChallengesByDaily_date(LocalDateTime daily_date);
    
}
