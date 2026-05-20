-- Extensions
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- Users
CREATE TABLE users (
                       id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       nickname      VARCHAR(20) NOT NULL,
                       games_played  INT NOT NULL DEFAULT 0,
                       high_score    INT NOT NULL DEFAULT 0,
                       created_at    TIMESTAMP NOT NULL DEFAULT NOW(),
                       updated_at    TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Locations
CREATE TABLE locations (
                           id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                           name        VARCHAR(255) NOT NULL,
                           city        VARCHAR(255) NOT NULL,
                           province    VARCHAR(50)  NOT NULL,
                           difficulty  VARCHAR(10)  NOT NULL,
                           latitude    DOUBLE PRECISION NOT NULL,
                           longitude   DOUBLE PRECISION NOT NULL,
                           active      BOOLEAN NOT NULL DEFAULT TRUE,
                           created_at  TIMESTAMP NOT NULL DEFAULT NOW(),
                           updated_at  TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Games
CREATE TABLE games (
                       id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       user_id       UUID NOT NULL REFERENCES users(id),
                       status        VARCHAR(20) NOT NULL DEFAULT 'IN_PROGRESS',
                       total_rounds  INT NOT NULL DEFAULT 5,
                       total_score   INT,
                       created_at    TIMESTAMP NOT NULL DEFAULT NOW(),
                       updated_at    TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Rounds
CREATE TABLE rounds (
                        id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                        game_id         UUID NOT NULL REFERENCES games(id),
                        location_id     UUID NOT NULL REFERENCES locations(id),
                        round_number    INT NOT NULL,
                        guess_lat       DOUBLE PRECISION,
                        guess_lng       DOUBLE PRECISION,
                        distance_meters INT,
                        score           INT,
                        created_at      TIMESTAMP NOT NULL DEFAULT NOW(),
                        answered_at     TIMESTAMP
);

-- Seed locations
INSERT INTO locations (name, city, province, difficulty, latitude, longitude) VALUES
                                                                                  ('Nkoninga Rd',          'Richards Bay',                     'KWAZULU_NATAL', 'EASY',   -28.738782205636063,  32.07325776264318),
                                                                                  ('Ramatlabama',          'Kopoleng',                         'NORTH_WEST',    'EASY',   -25.650128600186786,  25.57549470134341),
                                                                                  ('Soshanguve',           'Pretoria',                         'GAUTENG',       'MEDIUM', -25.565817534691515,  28.075874015271644),
                                                                                  ('School St',            'Magaliesburg',                     'GAUTENG',       'HARD',   -25.99546373356811,   27.540117501422188),
                                                                                  ('Thandi St',            'Potchefstroom',                    'NORTH_WEST',    'MEDIUM', -26.715876260829173,  27.125297635630552),
                                                                                  ('Reitz',                'Nketoana Local Municipality',      'FREE_STATE',    'EASY',   -27.801700548745806,  28.430519957142838),
                                                                                  ('Nothando',             'Senqu Local Municipality',         'EASTERN_CAPE',  'HARD',   -30.548880655488766,  27.6898100960995),
                                                                                  ('Brak',                 'Vereeniging',                      'GAUTENG',       'MEDIUM', -26.653012218223985,  27.97042433020901),
                                                                                  ('Tlokoeng',             'Elundini Local Municipality',      'EASTERN_CAPE',  'HARD',   -30.694471854435157,  28.498155631437676),
                                                                                  ('Heuningvlei',          'Joe Morolong Local Municipality',  'NORTHERN_CAPE', 'HARD',   -26.305956286214492,  23.138639883413646),
                                                                                  ('Reguit Street',        'Van Zylsrus',                      'NORTHERN_CAPE', 'MEDIUM', -26.87875468419265,   22.051523011548987),
                                                                                  ('Gray',                 'Knysna',                           'WESTERN_CAPE',  'MEDIUM', -34.02779089053674,   23.067376179626446),
                                                                                  ('Kruger National Park', 'Greater Giyani Local Municipality','LIMPOPO',       'EASY',   -23.11026828198417,   31.434806309605563),
                                                                                  ('4 Vlam Cres',          'Newcastle',                        'KWAZULU_NATAL', 'MEDIUM', -27.762994655377604,  29.951971158334082),
                                                                                  ('Ermelo',               'Msukaligwa',                       'MPUMALANGA',    'MEDIUM', -26.528953151522526,  29.977840640184212),
                                                                                  ('Fraserburg',           'Karoo Hoogland Local Municipality', 'NORTHERN_CAPE', 'EASY',  -31.914507000403464,  21.510253111601553),
                                                                                  ('Vezimanzi',            'Nquthu Local Municipality',        'KWAZULU_NATAL', 'HARD',   -28.65001717925406,   30.71680030276799),
                                                                                  ('Rietfontein',          'Mier Local Municipality',          'NORTHERN_CAPE', 'MEDIUM', -26.745972859076833,  20.028197064815338),
                                                                                  ('Zwelisha',             'Okhahlamba Local Municipality',    'KWAZULU_NATAL', 'HARD',   -28.675444503523636,  29.09523798057252),
                                                                                  ('KuNdlumbini',          'Mbhashe Local Municipality',       'EASTERN_CAPE',  'HARD',   -32.17896024529215,   28.76152860274096),
                                                                                  ('Ngcobo',               'Engcobo Local Municipality',       'EASTERN_CAPE',  'EASY',   -31.676583141506278,  27.99831663292021),
                                                                                  ('20 Glen Hurd Dr',      'Gqeberha',                         'EASTERN_CAPE',  'EASY',   -33.95751110115657,   25.562813413710334),
                                                                                  ('Robertson',            'Langeberg Local Municipality',     'WESTERN_CAPE',  'HARD',   -33.81838590439227,   19.895188156827768),
                                                                                  ('Kraalhoek',            'Moses Kotane Local Municipality',  'NORTH_WEST',    'HARD',   -24.91574447202435,   27.077157206590083),
                                                                                  ('Williston',            'Karoo Hoogland Local Municipality', 'NORTHERN_CAPE', 'MEDIUM', -31.34546658787159,  20.92447789437373),
                                                                                  ('Driekoppies',          'Nkomazi',                          'MPUMALANGA',    'MEDIUM', -25.69629860220893,   31.571910563176573),
                                                                                  ('Molteno',              'Inkwanca Local Municipality',      'EASTERN_CAPE',  'HARD',   -31.386355499284576,  26.35639415217971),
                                                                                  ('Koingnaas',            'Kamiesberg Local Municipality',    'NORTHERN_CAPE', 'MEDIUM', -30.192384488130934,  17.291716229688763),
                                                                                  ('Mkhomazi',             'Ndwedwe Local Municipality',       'KWAZULU_NATAL', 'HARD',   -29.47144734498311,   30.875861392248776),
                                                                                  ('Groblerburg',          'Lephalale Local Municipality',     'LIMPOPO',       'EASY',   -23.001071688314944,  27.944508459893704);