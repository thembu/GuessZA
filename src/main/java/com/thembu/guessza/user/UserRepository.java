package com.thembu.guessza.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsUserByUsername(String username);

   Optional<User> findUserByUsername(String username);

}
