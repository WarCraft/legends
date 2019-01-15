package gg.warcraft.legends.app.core.tutorial.handler;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.legends.api.core.service.SpawnLocationRepository;
import gg.warcraft.legends.api.core.tutorial.TutorialStep;
import gg.warcraft.legends.api.entity.LegendsTeam;
import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerCommandService;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.monolith.api.command.Command;
import gg.warcraft.monolith.api.command.CommandHandler;
import gg.warcraft.monolith.api.command.CommandSender;
import gg.warcraft.monolith.api.entity.player.event.PlayerConnectEvent;
import gg.warcraft.monolith.api.entity.service.EntityCommandService;
import gg.warcraft.monolith.api.world.Direction;
import gg.warcraft.monolith.api.world.DirectionUtils;
import gg.warcraft.monolith.api.world.WorldType;
import gg.warcraft.monolith.api.world.location.Location;
import gg.warcraft.monolith.api.world.location.LocationFactory;
import gg.warcraft.monolith.api.world.location.OrientedLocation;

import java.util.List;
import java.util.UUID;

public class TutorialStepHandler implements CommandHandler {
    private final LegendsPlayerQueryService playerQueryService;
    private final LegendsPlayerCommandService playerCommandService;
    private final EntityCommandService entityCommandService;
    private final DirectionUtils directionUtils;
    private final SpawnLocationRepository spawnLocationRepository;

    private Location startLocation;
    private Direction startOrientation;
    private Location raceLocation;
    private Direction raceOrientation;
    private Location classLocation;
    private Direction classOrientation;
    private Location gatheringLocation;
    private Direction gatheringOrientation;
    private Location siegeLocation;
    private Direction siegeOrientation;
    private Location finaleLocation;
    private Direction finaleOrientation;

    @Inject
    public TutorialStepHandler(LegendsPlayerQueryService playerQueryService,
                               LegendsPlayerCommandService playerCommandService,
                               EntityCommandService entityCommandService,
                               SpawnLocationRepository spawnLocationRepository,
                               DirectionUtils directionUtils,
                               LocationFactory locationFactory) {
        this.playerQueryService = playerQueryService;
        this.playerCommandService = playerCommandService;
        this.entityCommandService = entityCommandService;
        this.spawnLocationRepository = spawnLocationRepository;
        this.directionUtils = directionUtils;

        this.startLocation = locationFactory.createLocation(WorldType.THE_END, 196.5f, 163f, -19.5f);
        this.startOrientation = Direction.EAST;
        this.raceLocation = locationFactory.createLocation(WorldType.THE_END, 253.5f, 174f, 92.5f);
        this.raceOrientation = Direction.EAST;
        this.classLocation = locationFactory.createLocation(WorldType.THE_END, 357.5f, 191f, 34.5f);
        this.classOrientation = Direction.EAST;
        this.gatheringLocation = locationFactory.createLocation(WorldType.THE_END, 203.5f, 180f, -138.5f);
        this.gatheringOrientation = Direction.SOUTH;
        this.siegeLocation = locationFactory.createLocation(WorldType.THE_END, 288.5f, 165f, -191.5f);
        this.siegeOrientation = Direction.EAST;
        this.finaleLocation = locationFactory.createLocation(WorldType.THE_END, 414.5f, 178f, -112.5f);
        this.finaleOrientation = Direction.EAST;
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
                entityCommandService.teleport(playerId, raceLocation, raceOrientation);
                break;
            case RACE:
                if (player.getTeam() == LegendsTeam.WAYFARER) {
                    playerCommandService.sendNotification(playerId,
                            "Please choose a race by right clicking on one of the signs before continuing.");
                    return true;
                }
                nextStep = TutorialStep.CLASS;
                entityCommandService.teleport(playerId, classLocation, classOrientation);
                break;
            case CLASS:
                if (player.getSpecialization() == null) {
                    playerCommandService.sendNotification(playerId,
                            "Please choose a class by right clicking on one of the signs before continuing.");
                    return true;
                }
                nextStep = TutorialStep.GATHERING;
                entityCommandService.teleport(playerId, gatheringLocation, gatheringOrientation);
                break;
            case GATHERING:
                nextStep = TutorialStep.SIEGE;
                entityCommandService.teleport(playerId, siegeLocation, siegeOrientation);
                break;
            case SIEGE:
                nextStep = TutorialStep.FINALE;
                entityCommandService.teleport(playerId, finaleLocation, finaleOrientation);
                break;
            case FINALE:
                nextStep = TutorialStep.DONE;
                OrientedLocation spawn = spawnLocationRepository.getSpawn(player.getTeam());
                Direction direction = directionUtils.toDirection(spawn.getYaw());
                entityCommandService.teleport(playerId, spawn, direction);
                break;
        }

        if (nextStep != null) {
            playerCommandService.setTutorialStep(playerId, nextStep);
        }
        return true;
    }

    @Subscribe
    public void onPlayerConnectedEvent(PlayerConnectEvent event) {
        UUID playerId = event.getPlayerId();
        LegendsPlayer player = playerQueryService.getPlayer(playerId);
        TutorialStep currentStep = player.getTutorialStep();
        if (currentStep == TutorialStep.START) {
            entityCommandService.teleport(playerId, startLocation, startOrientation);
        }
    }
}
