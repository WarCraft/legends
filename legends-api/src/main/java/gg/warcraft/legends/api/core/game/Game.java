package gg.warcraft.legends.api.core.game;

import gg.warcraft.legends.api.entity.LegendsTeam;
import gg.warcraft.monolith.api.util.Duration;

import java.util.Map;

public interface Game {

    int getId();

    GameType getType();

    Map<LegendsTeam, Integer> getPoints();

    Map<String, LegendsTeam> getObjectives();

    Duration getTimer();

    String getStartTime();

    String getEndTime();

    GameCopyer getCopyer();
}
