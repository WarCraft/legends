CREATE TABLE statistic_archive
(
    player_id UUID        NOT NULL,
    statistic VARCHAR(32) NOT NULL,
    value     INTEGER     NOT NULL,
    date      DATE        NOT NULL,

    PRIMARY KEY (player_id, statistic, date)
);
