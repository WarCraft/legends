package gg.warcraft.legends.api.core.service;

import gg.warcraft.legends.api.entity.LegendsTeam;

public interface GatheringSpotOccupierRepository {

    LegendsTeam getTeamFor(String gatheringSpotId);

    void setTeamFor(String gatheringSpotId, LegendsTeam team);
}
