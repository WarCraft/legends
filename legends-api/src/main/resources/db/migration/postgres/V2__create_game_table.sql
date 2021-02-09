CREATE TABLE game
(
    id            BIGSERIAL PRIMARY KEY,
    start_time    TIMESTAMP WITH TIME ZONE NOT NULL,
    end_time      TIMESTAMP WITH TIME ZONE,
    timer         SMALLINT                 NOT NULL,
    objectives    TEXT                     NOT NULL,
    daily_points  TEXT                     NOT NULL,
    monthly_score TEXT                     NOT NULL
);
