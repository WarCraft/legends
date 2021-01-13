CREATE TABLE login_calendar
(
    player_id UUID PRIMARY KEY,
    day       SMALLINT NOT NULL,
    claimed   DATE     NOT NULL
);
