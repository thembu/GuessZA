package com.thembu.guessza.challenge;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChallengeRepository extends JpaRepository<Challenges , UUID> {

}
