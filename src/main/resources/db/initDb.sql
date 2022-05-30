DROP TABLE IF EXISTS random_words;
DROP TABLE IF EXISTS random_translations;
DROP TABLE IF EXISTS wordset_word;
DROP TABLE IF EXISTS user_word_training;
DROP TABLE IF EXISTS words;
DROP TABLE IF EXISTS wordsets;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS trainings;


CREATE TABLE users(
    id          SERIAL      PRIMARY KEY,
    email       TEXT        NOT NULL UNIQUE ,
    password    TEXT        NOT NULL
);
CREATE UNIQUE INDEX users_unique_email_idx ON users (email);

CREATE TABLE words(
  id            SERIAL      PRIMARY KEY,
  user_id       INTEGER     NOT NULL ,
  word          TEXT        NOT NULL,
  translation   TEXT        NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE wordsets(
  id            SERIAL      PRIMARY KEY,
  user_id       INTEGER     NOT NULL,
  name          TEXT        NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX unique_ws_name_user ON wordsets (user_id, name);

CREATE TABLE wordset_word(
    word_id     INTEGER     NOT NULL,
    wordset_id  INTEGER     NOT NULL,
    FOREIGN KEY (word_id) REFERENCES words (id) ON DELETE CASCADE,
    FOREIGN KEY (wordset_id) REFERENCES wordsets (id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX wordset_unique_word_idx ON wordset_word (word_id, wordset_id);

CREATE TABLE trainings(
    id          SERIAL      PRIMARY KEY,
    name        TEXT        UNIQUE NOT NULL
);

CREATE TABLE user_word_training(
    user_id     INTEGER     NOT NULL,
    word_id     INTEGER     NOT NULL,
    training_id INTEGER     NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (word_id) REFERENCES words (id) ON DELETE CASCADE,
    FOREIGN KEY (training_id) REFERENCES trainings (id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX user_word_training_unique_idx ON user_word_training (user_id, word_id, training_id);

CREATE TABLE random_words(
    word        TEXT        UNIQUE NOT NULL
);

CREATE TABLE random_translations(
    translation TEXT        UNIQUE NOT NULL
);