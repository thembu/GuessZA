-- V1__init_schema.sql
-- Initial schema: all four tables for GuessZA.
-- Flyway runs this once and records it in the flyway_schema_history table.

CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE users (
                       id           UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
                       nickname     VARCHAR(20)  NOT NULL,
                       games_played INT          NOT NULL DEFAULT 0,
                       high_score   INT          NOT NULL DEFAULT 0,
                       created_at   TIMESTAMP    NOT NULL DEFAULT NOW(),
                       updated_at   TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE TABLE locations (
                           id         UUID             PRIMARY KEY DEFAULT gen_random_uuid(),
                           name       VARCHAR(255)     NOT NULL,
                           city       VARCHAR(255)     NOT NULL,
                           province   VARCHAR(50)      NOT NULL,
                           difficulty VARCHAR(10)      NOT NULL,
                           latitude   DOUBLE PRECISION NOT NULL,
                           longitude  DOUBLE PRECISION NOT NULL,
                           active     BOOLEAN          NOT NULL DEFAULT TRUE,
                           created_at TIMESTAMP        NOT NULL DEFAULT NOW(),
                           updated_at TIMESTAMP        NOT NULL DEFAULT NOW()
);

CREATE TABLE games (
                       id           UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
                       user_id      UUID        NOT NULL REFERENCES users(id),
                       status       VARCHAR(20) NOT NULL DEFAULT 'IN_PROGRESS',
                       total_rounds INT         NOT NULL DEFAULT 5,
                       total_score  INT,
                       created_at   TIMESTAMP   NOT NULL DEFAULT NOW(),
                       updated_at   TIMESTAMP   NOT NULL DEFAULT NOW()
);

CREATE TABLE rounds (
                        id              UUID             PRIMARY KEY DEFAULT gen_random_uuid(),
                        game_id         UUID             NOT NULL REFERENCES games(id),
                        location_id     UUID             NOT NULL REFERENCES locations(id),
                        round_number    INT              NOT NULL,
                        guess_lat       DOUBLE PRECISION,
                        guess_lng       DOUBLE PRECISION,
                        distance_meters INT,
                        score           INT,
                        created_at      TIMESTAMP        NOT NULL DEFAULT NOW(),
                        answered_at     TIMESTAMP
);