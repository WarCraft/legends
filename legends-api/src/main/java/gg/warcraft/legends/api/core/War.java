package gg.warcraft.legends.api.core;

import gg.warcraft.monolith.api.entity.team.Team;

import java.util.Map;
import java.util.SortedMap;

public interface War extends Game {

    Map<Team, Integer> getWarScore();

    SortedMap<String, Map<Team, Integer>> getDailyPoints();
}
