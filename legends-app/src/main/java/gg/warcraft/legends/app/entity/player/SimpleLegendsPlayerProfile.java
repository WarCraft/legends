package gg.warcraft.legends.app.entity.player;

import gg.warcraft.legends.api.entity.player.LegendsPlayerProfile;
import gg.warcraft.monolith.api.entity.Team;
import gg.warcraft.monolith.app.entity.player.SimplePlayerProfile;

import java.util.Map;
import java.util.UUID;

public class SimpleLegendsPlayerProfile extends SimplePlayerProfile implements LegendsPlayerProfile {

    public SimpleLegendsPlayerProfile(UUID playerId, long timeConnected, long timeFirstConnected, long timeLastSeen,
                                      long timePlayed, Map<String, Integer> currencies,
                                      Map<String, Integer> lifetimeCurrencies, Team team) {
        super(playerId, timeConnected, timeFirstConnected, timeLastSeen, timePlayed, currencies, lifetimeCurrencies,
                team);
    }
}
