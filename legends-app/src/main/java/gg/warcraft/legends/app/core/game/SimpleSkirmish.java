package gg.warcraft.legends.app.core.game;

import gg.warcraft.legends.api.core.game.GameCopyer;
import gg.warcraft.legends.api.core.game.GameType;
import gg.warcraft.legends.api.core.game.Skirmish;
import gg.warcraft.monolith.api.entity.team.Team;
import gg.warcraft.monolith.api.util.Duration;

import java.util.Map;

public class SimpleSkirmish extends AbstractGame implements Skirmish {

    public SimpleSkirmish(int id, Map<Team, Integer> points, Map<String, Team> objectives, Duration timer,
                          String startTime, String endTime) {
        super(id, GameType.SKIRMISH, points, objectives, timer, startTime, endTime);
    }

    @Override
    public GameCopyer getCopyer() {
        return new SimpleSkirmishCopyer(this);
    }
}
