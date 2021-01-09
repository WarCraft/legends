CREATE TABLE completed_challenge
(
    id        VARCHAR(32) NOT NULL,
    player_id UUID        NOT NULL,

    PRIMARY KEY (id, player_id)
);
