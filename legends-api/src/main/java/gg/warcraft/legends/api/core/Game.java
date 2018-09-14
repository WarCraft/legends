package gg.warcraft.legends.api.core;

import gg.warcraft.monolith.api.entity.team.Team;
import gg.warcraft.monolith.api.util.Duration;

import java.util.Map;

public interface Game {

    int getId();

    GameType getType();

    Map<Team, Integer> getPoints();

    Map<String, Team> getObjectives();

    Duration getTimer();

    String getStartTime();

    String getEndTime();

    GameCopyer getCopyer();
}
