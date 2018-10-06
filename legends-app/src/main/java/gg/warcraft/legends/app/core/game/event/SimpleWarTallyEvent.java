package gg.warcraft.legends.app.core.game.event;

import gg.warcraft.legends.api.core.game.event.AbstractGameEvent;
import gg.warcraft.legends.api.core.game.event.WarTallyEvent;
import gg.warcraft.monolith.api.entity.team.Team;

import java.util.Map;

public class SimpleWarTallyEvent extends AbstractGameEvent implements WarTallyEvent {
    private final Map<Team, Integer> points;

    public SimpleWarTallyEvent(int gameId, Map<Team, Integer> points) {
        super(gameId);
        this.points = points;
    }

    @Override
    public int getPoints(Team team) {
        return points.getOrDefault(team, 0);
    }
}
