package gg.warcraft.legends.api.core;

import gg.warcraft.legends.api.entity.LegendsTeam;
import gg.warcraft.monolith.api.util.Duration;

import java.util.Map;

public interface Game {

    int getId();

    String getType();

    Map<LegendsTeam, Integer> getScore();

    Map<String, LegendsTeam> getObjectives();

    Duration getTimer();

    String getStartTime();

    String getEndTime();
}
