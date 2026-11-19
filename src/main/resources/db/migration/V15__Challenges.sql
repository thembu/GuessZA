CREATE TABLE challenges (
                            id          UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
                            code        VARCHAR(16) NOT NULL UNIQUE,          -- the link code; UNIQUE also indexes it for lookups
                            creator_id  UUID        NOT NULL REFERENCES users(id),
                            created_at  TIMESTAMP   NOT NULL DEFAULT NOW(),
                            expires_at  TIMESTAMP   NOT NULL
);

CREATE TABLE challenge_locations (
                                     challenge_id UUID NOT NULL REFERENCES challenges(id),
                                     round_number INT  NOT NULL,
                                     location_id  UUID NOT NULL REFERENCES locations(id),
                                     PRIMARY KEY (challenge_id, round_number)           -- one location per round slot
);

ALTER TABLE games ADD COLUMN challenge_id UUID REFERENCES challenges(id);   -- nullable: solo games stay NULL
ALTER TABLE games ADD CONSTRAINT uq_games_challenge_user UNIQUE (challenge_id, user_id);