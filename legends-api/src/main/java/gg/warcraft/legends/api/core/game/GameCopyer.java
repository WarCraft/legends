package gg.warcraft.legends.api.core.game;

import gg.warcraft.legends.api.entity.LegendsTeam;
import gg.warcraft.monolith.api.util.Duration;

import java.util.Map;

public interface GameCopyer {

    GameCopyer withPoints(Map<LegendsTeam, Integer> points);

    GameCopyer withObjectives(Map<String, LegendsTeam> objectives);

    GameCopyer withTimer(Duration timer);

    GameCopyer withEndTime(String endTime);

    Game copy();
}
