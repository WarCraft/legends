package gg.warcraft.legends.api.core;

import gg.warcraft.monolith.api.entity.team.Team;

public interface MetricsService {

    void recordPlayerCount(Team team, int count);

    void recordTotalPoints(Team team, int points);
}
