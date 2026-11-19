package com.thembu.guessza.challenge;


import com.thembu.guessza.user.User;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "challenges")
public class Challenges {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


    private String code;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;

    @CreationTimestamp
    private LocalDateTime created_at;

    private LocalDateTime expires_at;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public LocalDateTime getExpires_at() {
        return expires_at;
    }

    public void setExpires_at(LocalDateTime expires_at) {
        this.expires_at = expires_at;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }
}
