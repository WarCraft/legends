CREATE TABLE active_challenge
(
    challenge_id VARCHAR(32) PRIMARY KEY,
    is_daily     BOOL NOT NULL,
    is_monthly   BOOL NOT NULL
);
