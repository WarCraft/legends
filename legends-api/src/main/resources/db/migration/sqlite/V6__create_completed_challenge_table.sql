CREATE TABLE completed_challenge
(
    id        VARCHAR(32)   NOT NULL,
    player_id CHARACTER(32) NOT NULL,

    PRIMARY KEY (id, player_id)
);
