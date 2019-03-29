package gg.warcraft.legends.app.core.service;

import com.google.inject.Inject;
import gg.warcraft.legends.api.core.service.SpawnCommandService;
import gg.warcraft.legends.api.core.service.SpawnRepository;
import gg.warcraft.legends.api.entity.LegendsTeam;
import gg.warcraft.monolith.api.combat.CombatFactory;
import gg.warcraft.monolith.api.combat.PotionEffect;
import gg.warcraft.monolith.api.combat.PotionEffectType;
import gg.warcraft.monolith.api.core.TaskService;
import gg.warcraft.monolith.api.entity.player.Player;
import gg.warcraft.monolith.api.entity.player.service.PlayerCommandService;
import gg.warcraft.monolith.api.entity.player.service.PlayerQueryService;
import gg.warcraft.monolith.api.entity.service.EntityCommandService;
import gg.warcraft.monolith.api.entity.team.Team;
import gg.warcraft.monolith.api.util.Duration;
import gg.warcraft.monolith.api.util.TimeUtils;
import gg.warcraft.monolith.api.world.location.Location;

import java.util.UUID;

public class DefaultSpawnCommandService implements SpawnCommandService {
    private static final int WARP_DURATION_SECONDS = 10;
    private static final long WARP_COOLDOWN = 20 * TimeUtils.MILLIS_PER_MINUTE;

    private final EntityCommandService entityCommandService;
    private final PlayerCommandService playerCommandService;
    private final PlayerQueryService playerQueryService;
    private final SpawnRepository spawnRepository;
    private final TaskService taskService;
    private final Duration warpDuration;
    private final PotionEffect warpConfusion;

    @Inject
    public DefaultSpawnCommandService(EntityCommandService entityCommandService,
                                      PlayerCommandService playerCommandService,
                                      PlayerQueryService playerQueryService,
                                      SpawnRepository spawnRepository,
                                      CombatFactory combatFactory,
                                      TimeUtils timeUtils, TaskService taskService) {
        this.entityCommandService = entityCommandService;
        this.playerCommandService = playerCommandService;
        this.playerQueryService = playerQueryService;
        this.spawnRepository = spawnRepository;
        this.taskService = taskService;
        this.warpDuration = timeUtils.createDurationInSeconds(WARP_DURATION_SECONDS);
        this.warpConfusion = combatFactory.createPotionEffect(PotionEffectType.CONFUSION,
                0, warpDuration);
    }

    @Override
    public void warp(UUID playerId) {
        entityCommandService.addPotionEffect(playerId, warpConfusion);
        playerCommandService.sendNotification(playerId,
                "You can feel The Monolith's energy reaching out for you as it pulls you into The Realm's Weave.");

        taskService.runLater(() -> {
            Player player = playerQueryService.getPlayer(playerId);
            Team team = player.getTeam();
            if (team instanceof LegendsTeam) {
                Location spawnLocation = spawnRepository.getSpawn((LegendsTeam) player.getTeam());
                entityCommandService.teleport(playerId, spawnLocation);
            }
        }, warpDuration);

        long cooldown = System.currentTimeMillis() + WARP_COOLDOWN;
        spawnRepository.save(cooldown, playerId);
    }
}
