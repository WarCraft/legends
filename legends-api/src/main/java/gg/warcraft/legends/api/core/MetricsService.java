package gg.warcraft.legends.api.core;

import gg.warcraft.legends.api.entity.LegendsTeam;

public interface MetricsService {

    void recordPlayerCount(LegendsTeam team, int count);

    void recordTotalPoints(LegendsTeam team, int points);
}
