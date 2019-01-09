package gg.warcraft.legends.api.core.game.event;

import gg.warcraft.legends.api.entity.LegendsTeam;

public interface WarTallyEvent extends GameEvent {

    int getPoints(LegendsTeam team);
}
