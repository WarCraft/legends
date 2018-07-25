package gg.warcraft.legends.app.core;

import gg.warcraft.legends.api.core.Skirmish;
import gg.warcraft.legends.api.entity.LegendsTeam;
import gg.warcraft.monolith.api.util.Duration;

import java.util.Map;

public class SimpleSkirmish extends SimpleGame implements Skirmish {

    public SimpleSkirmish(int id, String type, Map<LegendsTeam, Integer> score, Map<String, LegendsTeam> objectives,
                          Duration timer, String startTime, String endTime) {
        super(id, type, score, objectives, timer, startTime, endTime);
    }
}
