package gg.warcraft.legends.app.core;

import gg.warcraft.legends.api.core.War;
import gg.warcraft.legends.api.entity.LegendsTeam;
import gg.warcraft.monolith.api.util.Duration;

import java.util.Map;

public class SimpleWar extends SimpleGame implements War {
    private final Map<LegendsTeam, Integer> points;

    public SimpleWar(int id, String type, Map<LegendsTeam, Integer> points, Map<LegendsTeam, Integer> score,
                     Map<String, LegendsTeam> objectives, Duration timer, String startTime, String endTime) {
        super(id, type, score, objectives, timer, startTime, endTime);
        this.points = points;
    }

    @Override
    public Map<LegendsTeam, Integer> getPoints() {
        return points;
    }
}
