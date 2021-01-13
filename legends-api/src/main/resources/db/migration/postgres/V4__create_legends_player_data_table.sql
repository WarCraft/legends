CREATE TABLE legends_player_data
(
    id                     UUID PRIMARY KEY,
    tutorial_step          SMALLINT NOT NULL,
    combat_class_cooldown  BIGINT   NOT NULL,
    launch_rewards_claimed BOOLEAN  NOT NULL,
    level_rewards_claimed  SMALLINT NOT NULL
);
