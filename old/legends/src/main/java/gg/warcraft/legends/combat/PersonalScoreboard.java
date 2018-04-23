package gg.warcraft.legends.combat;

import gg.warcraft.legends.entity.LegendsRace;
import gg.warcraft.monolith.Monolith;
import gg.warcraft.monolith.combat.event.MonolithAbilityCastEvent;
import gg.warcraft.monolith.event.EventService;
import gg.warcraft.monolith.event.MonolithEntityRaceChangedEvent;
import gg.warcraft.monolith.event.MonolithPlayerConnectEvent;
import gg.warcraft.monolith.event.MonolithPlayerDisconnectEvent;
import gg.warcraft.monolith.player.MonolithPlayer;
import gg.warcraft.monolith.player.PlayerService;
import gg.warcraft.monolith.util.Cancellable;
import gg.warcraft.monolith.util.Duration;
import gg.warcraft.monolith.util.MonolithTask;
import gg.warcraft.monolith.util.RunnableUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonalScoreboard implements Runnable, Cancellable, Listener {
    private static final String METADATA_KEY = PersonalScoreboard.class.getCanonicalName();
    private static final String COOLDOWN_OBJECTIVE = "Cooldowns";
    private static final String DUMMY_CRITERIA = "dummy";
    private static final String HEALTH_OBJECTIVE = "Health";
    private static final String HEALTH_CRITERIA = "health";

    static {
        EventService eventService = EventService.getInstance();
        eventService.registerForEvents(new Listener() {

            @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
            public void onMonolithAbilityCastEvent(MonolithAbilityCastEvent event) {
                List<MetadataValue> metadata = event.getCaster().getMetadata(METADATA_KEY);
                if (!metadata.isEmpty()) {
                    PersonalScoreboard scoreboard = (PersonalScoreboard) metadata.get(0).value();
                    String abilityClassName = event.getAbilityClass().getSimpleName();
                    String abilityName = StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(abilityClassName), " ");
                    scoreboard.track(abilityName, event.getConfiguration().getCooldown());
                }
            }

            @EventHandler(priority = EventPriority.MONITOR)
            public void onMonolithPlayerDisconnectEvent(MonolithPlayerDisconnectEvent event) {
                List<MetadataValue> metadata = event.getPlayer().getMetadata(METADATA_KEY);
                if (!metadata.isEmpty()) {
                    PersonalScoreboard scoreboard = (PersonalScoreboard) metadata.get(0).value();
                    scoreboard.cancel();
                    eventService.unregisterFromEvents(scoreboard);
                }
            }
        });
    }

    private final Scoreboard scoreboard;
    private final Map<String, Long> abilities;
    private final MonolithTask task;

    public PersonalScoreboard(Player player) {
        this(Monolith.getInstance(), PlayerService.getInstance(), player);
    }

    public PersonalScoreboard(Monolith monolith, PlayerService playerService, Player player) {
        this.scoreboard = monolith.getServer().getScoreboardManager().getNewScoreboard();
        initializeScoreboard(this.scoreboard, playerService.getOnlineMonolithPlayers());
        this.abilities = new HashMap<>();
        this.task = RunnableUtils.runTask(this, 1, 4);
        MetadataValue metadata = new FixedMetadataValue(monolith, this);
        player.setMetadata(METADATA_KEY, metadata);
        player.setScoreboard(scoreboard);

        Objective health = scoreboard.registerNewObjective(HEALTH_OBJECTIVE, HEALTH_CRITERIA);
        health.setDisplaySlot(DisplaySlot.BELOW_NAME);
        health.setDisplayName(ChatColor.RED + "❤");
    }

    private void initializeScoreboard(Scoreboard scoreboard, Collection<MonolithPlayer> onlinePlayers) {
        Team humans = scoreboard.registerNewTeam(LegendsRace.HUMAN.toString());
        humans.setPrefix(LegendsRace.HUMAN.getColor().toString());
        Team orcs = scoreboard.registerNewTeam(LegendsRace.ORC.toString());
        orcs.setPrefix(LegendsRace.ORC.getColor().toString());
        Team elves = scoreboard.registerNewTeam(LegendsRace.ELF.toString());
        elves.setPrefix(LegendsRace.ELF.getColor().toString());
        Team dwarves = scoreboard.registerNewTeam(LegendsRace.DWARF.toString());
        dwarves.setPrefix(LegendsRace.DWARF.getColor().toString());

        onlinePlayers.forEach(player -> {
            if (player.getRace() != null) {
                Team team = scoreboard.getTeam(player.getRace().toString());
                team.addEntry(player.getName());
            }
        });
    }

    private void track(String ability, Duration cooldown) {
        abilities.put(ability, System.currentTimeMillis() + cooldown.as(Duration.Type.MILLIS));
    }

    @Override
    public void run() {
        scoreboard.getObjectives().forEach(objective -> {
            if (objective.getDisplayName().equals(COOLDOWN_OBJECTIVE)) {
                objective.unregister();
            }
        });

        Map<String, Integer> cooldowns = new HashMap<>();
        abilities.forEach((key, value) -> {
            long cooldown = value - System.currentTimeMillis();
            if (cooldown > 0) {
                cooldowns.put(key, (int) (cooldown / 1000));
            }
        });

        if (!cooldowns.isEmpty()) {
            Objective objective = scoreboard.registerNewObjective(COOLDOWN_OBJECTIVE, DUMMY_CRITERIA);
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            cooldowns.forEach((key, value) -> {
                Score score = objective.getScore(key);
                score.setScore(value);
            });
        }
    }

    @Override
    public void cancel() {
        task.cancel();
    }

    @EventHandler
    public void onMonolithPlayerConnectEvent(MonolithPlayerConnectEvent event) {
        if (event.getPlayer().getRace() != null) {
            Team team = scoreboard.getTeam(event.getPlayer().getRace().toString());
            team.addEntry(event.getPlayer().getName());
        }
    }

    @EventHandler
    public void onMonolithEntityRaceChangedEvent(MonolithEntityRaceChangedEvent event) {
        if (event.getEntity() instanceof MonolithPlayer) {
            Team team = scoreboard.getTeam(event.getEntity().getRace().toString());
            team.addEntry(event.getEntity().getName());
        }
    }
}
