package gg.warcraft.legends.api.core;

import gg.warcraft.legends.api.entity.LegendsTeam;

import java.util.Map;

public interface War extends Game {

    Map<LegendsTeam, Integer> getPoints();
}
