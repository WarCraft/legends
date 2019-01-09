package gg.warcraft.legends.api.core.game;

import gg.warcraft.legends.api.entity.LegendsTeam;

import java.util.Map;
import java.util.SortedMap;

public interface War extends Game {

    Map<LegendsTeam, Integer> getWarScore();

    SortedMap<String, Map<LegendsTeam, Integer>> getDailyPoints();

    @Override
    WarCopyer getCopyer();
}
