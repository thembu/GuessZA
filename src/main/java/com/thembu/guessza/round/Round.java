package com.thembu.guessza.round;

import com.thembu.guessza.game.Game;
import com.thembu.guessza.location.Location;
import jakarta.persistence.*;
import jdk.jfr.Timestamp;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "rounds")
public class Round {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @ManyToOne
    @JoinColumn(name="location_id")
    private Location location;

    private Integer roundNumber;
    private Double  guessLat;
    private Double  guessLng;
    private Integer distanceMeters;
    private Integer score;

    @CreationTimestamp
    private LocalDateTime createdAt;


    private  LocalDateTime answeredAt;

    public Round() {

    }


    public UUID getId() {
        return id;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Integer getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(Integer roundNumber) {
        this.roundNumber = roundNumber;
    }

    public Double getGuessLat() {
        return guessLat;
    }

    public void setGuessLat(Double guessLat) {
        this.guessLat = guessLat;
    }

    public Double getGuessLng() {
        return guessLng;
    }

    public void setGuessLng(Double guessLng) {
        this.guessLng = guessLng;
    }

    public Integer getDistanceMeters() {
        return distanceMeters;
    }

    public void setDistanceMeters(Integer distanceMeters) {
        this.distanceMeters = distanceMeters;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


    public LocalDateTime getAnsweredAt() {
        return answeredAt;
    }

    public void setAnsweredAt(LocalDateTime answeredAt) {
        this.answeredAt = answeredAt;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
