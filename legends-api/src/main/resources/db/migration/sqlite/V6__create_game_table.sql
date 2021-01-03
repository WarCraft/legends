CREATE TABLE player_data
(
    id            BIGINT PRIMARY KEY AUTOINCREMENT,
    start_time    VARCHAR(32) NOT NULL,
    end_time      VARCHAR(32),
    timer         SMALLINT    NOT NULL,
    objectives    TEXT        NOT NULL,
    daily_points  TEXT        NOT NULL,
    monthly_score TEXT        NOT NULL
);
