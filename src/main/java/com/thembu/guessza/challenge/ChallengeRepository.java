package com.thembu.guessza.challenge;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface ChallengeRepository extends JpaRepository<Challenges , UUID> {

     Optional<Challenges> findChallengesByCode(String code);

     Optional<Challenges> findByDailyDate(LocalDate daily_date);
    
}
