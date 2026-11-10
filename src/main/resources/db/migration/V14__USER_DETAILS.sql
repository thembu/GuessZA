DELETE FROM visited_locations;
DELETE FROM rounds;
DELETE FROM games;
DELETE FROM users;

ALTER TABLE  users ADD COLUMN username varchar(20) UNIQUE NOT NULL;
ALTER TABLE  users ADD COLUMN password_hash varchar(255) UNIQUE  NOT NULL;