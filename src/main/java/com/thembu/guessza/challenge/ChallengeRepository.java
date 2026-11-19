package com.thembu.guessza.challenge;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ChallengeRepository extends JpaRepository<Challenges , UUID> {

    public Optional<Challenges> findChallengesByCode(String code);
    
}
