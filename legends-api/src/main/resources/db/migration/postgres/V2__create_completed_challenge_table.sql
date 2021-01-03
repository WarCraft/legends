CREATE TABLE completed_challenge
(
    player_id    UUID        NOT NULL,
    challenge_id VARCHAR(32) NOT NULL,
    is_daily     BOOLEAN     NOT NULL,
    is_monthly   BOOLEAN     NOT NULL,

    PRIMARY KEY (player_id, challenge_id)
);
