package gg.warcraft.legends.api.core;

import gg.warcraft.monolith.api.entity.team.Team;
import gg.warcraft.monolith.api.util.Duration;

import java.util.Map;

public interface GameCopyer {

    GameCopyer withPoints(Map<Team, Integer> points);

    GameCopyer withObjectives(Map<String, Team> objectives);

    GameCopyer withTimer(Duration timer);

    GameCopyer withEndTime(String endTime);

    Game copy();
}
