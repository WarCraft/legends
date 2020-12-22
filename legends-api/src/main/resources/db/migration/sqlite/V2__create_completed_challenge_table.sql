CREATE TABLE completed_challenge
(
    player_id    CHAR(32)    NOT NULL,
    challenge_id VARCHAR(32) NOT NULL,
    is_daily     BOOL        NOT NULL,
    is_monthly   BOOL        NOT NULL,

    PRIMARY KEY (player_id, challenge_id)
);
