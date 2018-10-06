package gg.warcraft.legends.api.core.game.event;

import gg.warcraft.monolith.api.entity.team.Team;

public interface WarTallyEvent extends GameEvent {

    int getPoints(Team team);
}
