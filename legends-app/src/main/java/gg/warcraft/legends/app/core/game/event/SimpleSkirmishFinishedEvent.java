package gg.warcraft.legends.app.core.game.event;

import gg.warcraft.legends.api.core.game.event.AbstractGameEvent;
import gg.warcraft.legends.api.core.game.event.SkirmishFinishedEvent;
import gg.warcraft.monolith.api.entity.team.Team;

public class SimpleSkirmishFinishedEvent extends AbstractGameEvent implements SkirmishFinishedEvent {
    private final Team winner;

    public SimpleSkirmishFinishedEvent(int gameId, Team winner) {
        super(gameId);
        this.winner = winner;
    }

    @Override
    public Team getWinner() {
        return winner;
    }
}
