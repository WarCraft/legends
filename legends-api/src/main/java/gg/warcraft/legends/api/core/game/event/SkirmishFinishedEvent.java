package gg.warcraft.legends.api.core.game.event;

import gg.warcraft.monolith.api.entity.team.Team;

public interface SkirmishFinishedEvent extends GameEvent {

    Team getWinner();
}
