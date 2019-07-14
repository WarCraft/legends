package gg.warcraft.legends.app.core.tutorial.handler;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.legends.api.core.service.SpawnRepository;
import gg.warcraft.legends.api.core.tutorial.TutorialStep;
import gg.warcraft.legends.api.entity.LegendsTeam;
import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerCommandService;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.legends.api.shop.SpecializationId;
import gg.warcraft.legends.api.shop.profile.service.ShopProfileCommandService;
import gg.warcraft.monolith.api.command.Command;
import gg.warcraft.monolith.api.command.CommandHandler;
import gg.warcraft.monolith.api.command.CommandSender;
import gg.warcraft.monolith.api.core.TaskService;
import gg.warcraft.monolith.api.entity.player.event.PlayerConnectEvent;
import gg.warcraft.monolith.api.entity.player.event.PlayerPreRespawnEvent;
import gg.warcraft.monolith.api.entity.service.EntityCommandService;
import gg.warcraft.monolith.api.world.Direction;
import gg.warcraft.monolith.api.world.DirectionUtils;
import gg.warcraft.monolith.api.world.World;
import gg.warcraft.monolith.api.world.location.Location;
import gg.warcraft.monolith.api.world.location.Orientation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TutorialStepHandler implements CommandHandler {
    private static final Map<String, SpecializationId> specializationNameToId;

    static {
        specializationNameToId = new HashMap<>();
        specializationNameToId.put("Armsmaster", SpecializationId.ARMSMASTER);
        specializationNameToId.put("Berserker", SpecializationId.BERSERKER);
        specializationNameToId.put("Hunter", SpecializationId.HUNTER);
        specializationNameToId.put("Marksman", SpecializationId.MARKSMAN);
        specializationNameToId.put("Tempest", SpecializationId.TEMPEST);
        specializationNameToId.put("Wizard", SpecializationId.WIZARD);
    }

    private final LegendsPlayerQueryService playerQueryService;
    private final LegendsPlayerCommandService playerCommandService;
    private final EntityCommandService entityCommandService;
    private final ShopProfileCommandService shopProfileCommandService;
    private final DirectionUtils directionUtils;
    private final SpawnRepository spawnRepository;
    private final TaskService taskService;

    private Location startLocation;
    private Orientation startOrientation;
    private Location raceLocation;
    private Orientation raceOrientation;
    private Location classLocation;
    private Orientation classOrientation;
    private Location gatheringLocation;
    private Orientation gatheringOrientation;
    private Location siegeLocation;
    private Orientation siegeOrientation;
    private Location finaleLocation;
    private Orientation finaleOrientation;

    @Inject
    public TutorialStepHandler(LegendsPlayerQueryService playerQueryService,
                               LegendsPlayerCommandService playerCommandService,
                               EntityCommandService entityCommandService,
                               ShopProfileCommandService shopProfileCommandService,
                               SpawnRepository spawnRepository,
                               DirectionUtils directionUtils,
                               TaskService taskService) {
        this.playerQueryService = playerQueryService;
        this.playerCommandService = playerCommandService;
        this.entityCommandService = entityCommandService;
        this.shopProfileCommandService = shopProfileCommandService;
        this.spawnRepository = spawnRepository;
        this.directionUtils = directionUtils;
        this.taskService = taskService;

        this.startLocation = new Location(World.THE_END, 196.5f, 163f, -19.5f);
        this.startOrientation = Orientation.apply(directionUtils.toVector(Direction.EAST));
        this.raceLocation = new Location(World.THE_END, 253.5f, 174f, 92.5f);
        this.raceOrientation = Orientation.apply(directionUtils.toVector(Direction.EAST));
        this.classLocation = new Location(World.THE_END, 357.5f, 191f, 34.5f);
        this.classOrientation = Orientation.apply(directionUtils.toVector(Direction.EAST));
        this.gatheringLocation = new Location(World.THE_END, 203.5f, 180f, -138.5f);
        this.gatheringOrientation = Orientation.apply(directionUtils.toVector(Direction.SOUTH));
        this.siegeLocation = new Location(World.THE_END, 288.5f, 165f, -191.5f);
        this.siegeOrientation = Orientation.apply(directionUtils.toVector(Direction.EAST));
        this.finaleLocation = new Location(World.THE_END, 414.5f, 178f, -112.5f);
        this.finaleOrientation = Orientation.apply(directionUtils.toVector(Direction.EAST));
    }

    private Location getLocationForStep(TutorialStep step) {
        if (step == null) {
            return startLocation;
        }

        switch (step) {
            case RACE:
                return raceLocation;
            case CLASS:
                return classLocation;
            case GATHERING:
                return gatheringLocation;
            case SIEGE:
                return siegeLocation;
            case FINALE:
                return finaleLocation;
            case START:
            default:
                return startLocation;
        }
    }

    private void teleportToStep(UUID playerId, TutorialStep step) {
        if (step == null) {
            entityCommandService.teleport(playerId, startLocation, Direction.EAST);
            return;
        }

        switch (step) {
            case START:
                entityCommandService.teleport(playerId, startLocation, Direction.EAST);
                break;
            case RACE:
                entityCommandService.teleport(playerId, raceLocation, Direction.EAST);
                break;
            case CLASS:
                entityCommandService.teleport(playerId, classLocation, Direction.EAST);
                break;
            case GATHERING:
                entityCommandService.teleport(playerId, gatheringLocation, Direction.SOUTH);
                break;
            case SIEGE:
                entityCommandService.teleport(playerId, siegeLocation, Direction.EAST);
                break;
            case FINALE:
                entityCommandService.teleport(playerId, finaleLocation, Direction.EAST);
                break;
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, List<String> arguments) {
        if (!sender.isPlayer()) {
            return false;
        }

        UUID playerId = sender.getPlayerId();
        LegendsPlayer player = playerQueryService.getPlayer(playerId);
        TutorialStep nextStep = null;
        switch (player.getTutorialStep()) {
            case START:
                nextStep = TutorialStep.RACE;
                break;
            case RACE:
                LegendsTeam team = player.getTeam();
                if (team == null || team == LegendsTeam.WAYFARER) {
                    playerCommandService.sendNotification(playerId,
                            "Please choose a race by right clicking on one of the signs before continuing.");
                    return true;
                }
                nextStep = TutorialStep.CLASS;
                break;
            case CLASS:
                if (player.getSpecialization() == null) {
                    playerCommandService.sendNotification(playerId,
                            "Please choose a class by right clicking on one of the signs before continuing.");
                    return true;
                }
                nextStep = TutorialStep.GATHERING;
                break;
            case GATHERING:
                nextStep = TutorialStep.SIEGE;
                break;
            case SIEGE:
                nextStep = TutorialStep.FINALE;
                break;
            case FINALE:
                nextStep = TutorialStep.DONE;
                Location spawn = spawnRepository.getSpawn(player.getTeam());
                // TODO Direction direction = directionUtils.toDirection(spawn.getYaw());
                entityCommandService.teleport(playerId, spawn, Direction.NORTH);

                // Unlock current player specialization
                String specializationName = player.getSpecialization().getName();
                SpecializationId specializationId = specializationNameToId.get(specializationName);
                if (specializationId != null) {
                    shopProfileCommandService.unlockSpecialization(specializationId, playerId, false);
                } else {
                    System.out.println("WARN Failed to unlock " + specializationName +
                            " specialization for " + player.getName() + " after completing tutorial");
                }
                break;
        }

        if (nextStep != null) {
            playerCommandService.setTutorialStep(playerId, nextStep);
            teleportToStep(playerId, nextStep);
        }
        return true;
    }

    @Subscribe
    public void onPlayerConnectedEvent(PlayerConnectEvent event) {
        UUID playerId = event.getPlayerId();
        LegendsPlayer player = playerQueryService.getPlayer(playerId);
        TutorialStep currentStep = player.getTutorialStep();
        if (currentStep != TutorialStep.DONE) {
            if (currentStep == null) {
                playerCommandService.setTutorialStep(playerId, TutorialStep.START);
            }
            taskService.runNextTick(() -> teleportToStep(playerId, currentStep));
        }
    }

    @Subscribe
    public void onPlayerRespawnEvent(PlayerPreRespawnEvent event) {
        UUID playerId = event.getPlayerId();
        LegendsPlayer player = playerQueryService.getPlayer(playerId);
        TutorialStep currentStep = player.getTutorialStep();

        if (currentStep != TutorialStep.DONE) {
            Location teleportLocation = getLocationForStep(currentStep);
            event.setLocation(teleportLocation);
        }
    }
}
