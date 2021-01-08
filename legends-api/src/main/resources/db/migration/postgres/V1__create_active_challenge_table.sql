CREATE TABLE active_challenge
(
    challenge  VARCHAR(32) PRIMARY KEY,
    is_daily   BOOLEAN NOT NULL,
    is_monthly BOOLEAN NOT NULL
);
