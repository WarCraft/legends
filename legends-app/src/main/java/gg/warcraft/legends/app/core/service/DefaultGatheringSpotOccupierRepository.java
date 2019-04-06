package gg.warcraft.legends.app.core.service;

import com.google.inject.Singleton;
import gg.warcraft.legends.api.core.service.GatheringSpotOccupierRepository;
import gg.warcraft.legends.api.entity.LegendsTeam;

import java.util.HashMap;

@Singleton
public class DefaultGatheringSpotOccupierRepository implements GatheringSpotOccupierRepository {
    private final HashMap<String, LegendsTeam> controllers;

    public DefaultGatheringSpotOccupierRepository() {
        this.controllers = new HashMap<>();
    }

    @Override
    public LegendsTeam getTeamFor(String gatheringSpotId) {
        return controllers.get(gatheringSpotId);
    }

    @Override
    public void setTeamFor(String gatheringSpotId, LegendsTeam team) {
        controllers.put(gatheringSpotId, team);
    }
}
