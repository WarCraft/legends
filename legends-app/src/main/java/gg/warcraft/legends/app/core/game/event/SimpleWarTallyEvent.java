package gg.warcraft.legends.app.core.game.event;

import gg.warcraft.legends.api.core.game.event.AbstractGameEvent;
import gg.warcraft.legends.api.core.game.event.WarTallyEvent;
import gg.warcraft.legends.api.entity.LegendsTeam;

import java.util.Map;

public class SimpleWarTallyEvent extends AbstractGameEvent implements WarTallyEvent {
    private final Map<LegendsTeam, Integer> points;

    public SimpleWarTallyEvent(int gameId, Map<LegendsTeam, Integer> points) {
        super(gameId);
        this.points = points;
    }

    @Override
    public int getPoints(LegendsTeam team) {
        return points.getOrDefault(team, 0);
    }
}
