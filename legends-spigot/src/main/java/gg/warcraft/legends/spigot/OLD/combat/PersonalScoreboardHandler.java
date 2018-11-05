package gg.warcraft.legends.spigot.OLD.combat;

import com.google.common.eventbus.Subscribe;
import gg.warcraft.combat.api.ability.event.AbilityCastEvent;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.monolith.api.core.EventService;
import gg.warcraft.monolith.api.core.TaskService;
import gg.warcraft.monolith.api.entity.player.event.PlayerConnectEvent;
import gg.warcraft.monolith.api.entity.player.event.PlayerDisconnectEvent;
import gg.warcraft.monolith.api.util.Duration;
import gg.warcraft.monolith.api.util.TimeUtils;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PersonalScoreboardHandler {
    private static final String METADATA_KEY = PersonalScoreboardHandler.class.getCanonicalName();
    private static final String COOLDOWN_OBJECTIVE = "Cooldowns";
    private static final String DUMMY_CRITERIA = "dummy";
    private static final String HEALTH_OBJECTIVE = "Health";
    private static final String HEALTH_CRITERIA = "health";

    private final EventService eventService;
    private final Map<UUID, PersonalScoreboard> scoreboards;

    private final Server server;
    private final Plugin plugin;
    private final LegendsPlayerQueryService legendsPlayerQueryService;
    private final TaskService taskService;
    private final TimeUtils timeUtils;

    public PersonalScoreboardHandler(EventService eventService, Server server, Plugin plugin,
                                     LegendsPlayerQueryService legendsPlayerQueryService, TaskService taskService,
                                     TimeUtils timeUtils) {
        this.eventService = eventService;
        this.scoreboards = new HashMap<>();

        this.server = server;
        this.plugin = plugin;
        this.legendsPlayerQueryService = legendsPlayerQueryService;
        this.taskService = taskService;
        this.timeUtils = timeUtils;
    }

    @Subscribe
    public void onPlayerConnectEvent(PlayerConnectEvent event) {
        Player spigotPlayer = server.getPlayer(event.getPlayerId());
        PersonalScoreboard scoreboard = new PersonalScoreboard(server, plugin, legendsPlayerQueryService, taskService,
                timeUtils, spigotPlayer);
        scoreboards.put(event.getPlayerId(), scoreboard);
        eventService.subscribe(scoreboard);
    }

    @Subscribe
    public void onMonolithAbilityCastEvent(AbilityCastEvent event) {
        PersonalScoreboard scoreboard = scoreboards.get(event.getCasterId());
        if (scoreboard != null) {
            String abilityName = event.getAbilityName();
            Duration cooldown = event.getSnapshot().getCooldown();
            scoreboard.track(abilityName, cooldown);
        }
    }

    @Subscribe
    public void onMonolithPlayerDisconnectEvent(PlayerDisconnectEvent event) {
        PersonalScoreboard scoreboard = scoreboards.remove(event.getPlayerId());
        if (scoreboard != null) {
            scoreboard.cancel();
            eventService.unsubscribe(scoreboard);
        }
    }
}
