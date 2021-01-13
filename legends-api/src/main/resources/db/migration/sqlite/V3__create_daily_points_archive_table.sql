CREATE TABLE daily_points_archive
(
    game_id      INTEGER     NOT NULL,
    date         VARCHAR(32) NOT NULL,
    daily_points TEXT        NOT NULL,

    PRIMARY KEY (game_id, date),
    CONSTRAINT daily_points_archive_game_fk FOREIGN KEY (game_id) REFERENCES game (id)
);
