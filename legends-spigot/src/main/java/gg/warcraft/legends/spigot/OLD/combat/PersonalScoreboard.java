package gg.warcraft.legends.spigot.OLD.combat;

import com.google.common.eventbus.Subscribe;
import gg.warcraft.legends.api.entity.LegendsTeam;
import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.monolith.api.core.Task;
import gg.warcraft.monolith.api.core.TaskService;
import gg.warcraft.monolith.api.entity.event.EntityTeamChangedEvent;
import gg.warcraft.monolith.api.entity.player.event.PlayerConnectEvent;
import gg.warcraft.monolith.api.util.Cancellable;
import gg.warcraft.monolith.api.util.Duration;
import gg.warcraft.monolith.api.util.TimeUtils;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PersonalScoreboard implements Runnable, Cancellable {
    private static final String METADATA_KEY = PersonalScoreboard.class.getCanonicalName();
    private static final String COOLDOWN_OBJECTIVE = "Cooldowns";
    private static final String DUMMY_CRITERIA = "dummy";
    private static final String HEALTH_OBJECTIVE = "Health";
    private static final String HEALTH_CRITERIA = "health";

    private final LegendsPlayerQueryService legendsPlayerQueryService;
    private final Scoreboard scoreboard;
    private final Map<String, Long> abilities;
    private final Task task;

    public PersonalScoreboard(Server server, Plugin plugin, LegendsPlayerQueryService legendsPlayerQueryService,
                              TaskService taskService, TimeUtils timeUtils, Player player) {
        this.legendsPlayerQueryService = legendsPlayerQueryService;
        this.scoreboard = server.getScoreboardManager().getNewScoreboard();
        initializeScoreboard(this.scoreboard, legendsPlayerQueryService.getOnlinePlayers());
        this.abilities = new HashMap<>();
        this.task = taskService.runTask(this, timeUtils.oneTick(), timeUtils.createDurationInTicks(4));
        MetadataValue metadata = new FixedMetadataValue(plugin, this);
        player.setMetadata(METADATA_KEY, metadata);
        player.setScoreboard(scoreboard);

        Objective health = scoreboard.registerNewObjective(HEALTH_OBJECTIVE, HEALTH_CRITERIA);
        health.setDisplaySlot(DisplaySlot.BELOW_NAME);
        health.setDisplayName(ChatColor.RED + "❤");
    }

    private void initializeScoreboard(Scoreboard scoreboard, Collection<LegendsPlayer> onlinePlayers) {
        Team humans = scoreboard.registerNewTeam(LegendsTeam.HUMAN.toString());
        humans.setPrefix(LegendsTeam.HUMAN.getChatColor().toString());
        Team orcs = scoreboard.registerNewTeam(LegendsTeam.ORC.toString());
        orcs.setPrefix(LegendsTeam.ORC.getChatColor().toString());
        Team elves = scoreboard.registerNewTeam(LegendsTeam.ELF.toString());
        elves.setPrefix(LegendsTeam.ELF.getChatColor().toString());
        Team dwarves = scoreboard.registerNewTeam(LegendsTeam.DWARF.toString());
        dwarves.setPrefix(LegendsTeam.DWARF.getChatColor().toString());

        onlinePlayers.forEach(player -> {
            if (player.getTeam() != null) {
                Team team = scoreboard.getTeam(player.getTeam().toString());
                team.addEntry(player.getName());
            }
        });
    }

    public void track(String ability, Duration cooldown) {
        abilities.put(ability, System.currentTimeMillis() + cooldown.inMillis());
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

    @Subscribe
    public void onPlayerConnectEvent(PlayerConnectEvent event) {
        LegendsPlayer player = legendsPlayerQueryService.getPlayer(event.getPlayerId());
        if (player.getTeam() != null) {
            Team team = scoreboard.getTeam(player.getTeam().toString());
            if (team != null) {
                team.addEntry(player.getName());
            }
        }
    }

    @Subscribe
    public void onEntityTeamChangedEvent(EntityTeamChangedEvent event) {
        LegendsPlayer player = legendsPlayerQueryService.getPlayer(event.getEntityId());
        if (player != null) {
            Team team = scoreboard.getTeam(event.getNewTeam().toString());
            if (team != null) {
                team.addEntry(player.getName());
            }
        }
    }
}
