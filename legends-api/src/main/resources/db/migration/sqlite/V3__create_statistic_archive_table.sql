CREATE TABLE statistic_archive
(
    player_id CHARACTER(32) NOT NULL,
    statistic VARCHAR(32)   NOT NULL,
    value     INTEGER       NOT NULL,
    date      VARCHAR(32)   NOT NULL,

    PRIMARY KEY (player_id, statistic, date)
);
