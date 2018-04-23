package gg.warcraft.legends.objective;

import gg.warcraft.monolith.entity.Race;

public interface Scoreboard {

    int getScore(Race race);

    void addScore(Race race, int score);
}
