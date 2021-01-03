CREATE TABLE active_challenge
(
    challenge_id VARCHAR(32) NOT NULL PRIMARY KEY,
    is_daily     BOOLEAN     NOT NULL,
    is_monthly   BOOLEAN     NOT NULL
);
