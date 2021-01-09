CREATE TABLE login_calendar
(
    player_id CHARACTER(32) NOT NULL PRIMARY KEY,
    day       SMALLINT      NOT NULL,
    claimed   VARCHAR(16)   NOT NULL
);
