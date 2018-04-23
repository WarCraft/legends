package gg.warcraft.legends.metrics;

import gg.warcraft.legends.entity.LegendsRace;
import gg.warcraft.monolith.entity.Race;
import gg.warcraft.monolith.event.MonolithPlayerConnectEvent;
import gg.warcraft.monolith.event.MonolithPlayerDisconnectEvent;
import gg.warcraft.monolith.player.MonolithPlayer;
import gg.warcraft.monolith.util.RunnableUtils;
import gg.warcraft.monolith.util.TimeUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PlayerCountListener implements Listener {
    private static final long ONE_MINUTE = TimeUtils.TICKS_PER_MINUTE;
    private static final long FIVE_MINUTES = TimeUtils.TICKS_PER_MINUTE * 5;

    private final Map<Race, Set<MonolithPlayer>> playersByRace;

    public PlayerCountListener() {
        playersByRace = new HashMap<>();
        playersByRace.put(LegendsRace.HUMAN, new HashSet<>());
        playersByRace.put(LegendsRace.ORC, new HashSet<>());

        RunnableUtils.runTask(this::publishMetrics, ONE_MINUTE, FIVE_MINUTES);
    }

    private void publishMetrics() {
        MetricsService.getInstance().publishPlayerCountMetric(
                playersByRace.get(LegendsRace.HUMAN).size(),
                playersByRace.get(LegendsRace.ORC).size()
        );
    }

    @EventHandler
    public void onMonolithPlayerConnectEvent(MonolithPlayerConnectEvent event) {
        Race race = event.getPlayer().getRace();
        Set<MonolithPlayer> players = playersByRace.get(race);
        if (players != null) {
            players.add(event.getPlayer());
        } else {
            System.out.println("Could not add player to race set in PlayerCountListener!");
        }
    }

    @EventHandler
    public void onMonolithPlayerDisconnectEvent(MonolithPlayerDisconnectEvent event) {
        Race race = event.getPlayer().getRace();
        Set<MonolithPlayer> players = playersByRace.get(race);
        if (players != null) {
            players.remove(event.getPlayer());
        } else {
            System.out.println("Could not remove player from race set in PlayerCountListener!");
        }
    }
}
