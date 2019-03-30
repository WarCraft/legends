package gg.warcraft.legends.app.core.tutorial.handler;

import com.google.inject.Inject;
import gg.warcraft.combat.api.specialization.service.SpecializationCommandService;
import gg.warcraft.legends.api.core.service.SpawnRepository;
import gg.warcraft.legends.api.core.tutorial.TutorialStep;
import gg.warcraft.legends.api.entity.LegendsTeam;
import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerCommandService;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.monolith.api.command.Command;
import gg.warcraft.monolith.api.command.CommandHandler;
import gg.warcraft.monolith.api.command.CommandSender;
import gg.warcraft.monolith.api.entity.service.EntityCommandService;
import gg.warcraft.monolith.api.entity.team.service.TeamCommandService;
import gg.warcraft.monolith.api.world.Direction;
import gg.warcraft.monolith.api.world.DirectionUtils;
import gg.warcraft.monolith.api.world.location.OrientedLocation;

import java.util.List;
import java.util.UUID;

public class TutorialCommandHandler implements CommandHandler {
    private final LegendsPlayerQueryService playerQueryService;
    private final LegendsPlayerCommandService playerCommandService;
    private final EntityCommandService entityCommandService;
    private final TeamCommandService teamCommandService;
    private final SpecializationCommandService specializationCommandService;
    private final DirectionUtils directionUtils;
    private final SpawnRepository spawnRepository;

    @Inject
    public TutorialCommandHandler(LegendsPlayerQueryService playerQueryService,
                                  LegendsPlayerCommandService playerCommandService,
                                  EntityCommandService entityCommandService,
                                  TeamCommandService teamCommandService,
                                  SpecializationCommandService specializationCommandService,
                                  SpawnRepository spawnRepository,
                                  DirectionUtils directionUtils) {
        this.playerQueryService = playerQueryService;
        this.playerCommandService = playerCommandService;
        this.entityCommandService = entityCommandService;
        this.teamCommandService = teamCommandService;
        this.specializationCommandService = specializationCommandService;
        this.directionUtils = directionUtils;
        this.spawnRepository = spawnRepository;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, List<String> arguments) {
        if (!sender.isPlayer()) {
            return false;
        }

        UUID playerId = sender.getPlayerId();
        LegendsPlayer player = playerQueryService.getPlayer(playerId);
        TutorialStep currentStep = player.getTutorialStep();
        if (currentStep == TutorialStep.DONE) {
            return true;
        }

        switch (label) {
            case "setracetutorial":
                if (!arguments.isEmpty()) {
                    String argument = arguments.get(0);
                    LegendsTeam race = LegendsTeam.valueOf(argument);
                    teamCommandService.setTeam(race, playerId);
                }
                return true;
            case "setclasstutorial":
                if (!arguments.isEmpty()) {
                    String argument = arguments.get(0);
                    specializationCommandService.setSpecialization(argument, playerId);
                }
                return true;
            case "skiptutorial":
                if (currentStep == null) {
                    playerCommandService.sendNotification(playerId,
                            "Please finish the race and class steps before skipping the rest of the tutorial.");
                    return true;
                }
                switch (currentStep) {
                    case START:
                    case RACE:
                    case CLASS:
                        playerCommandService.sendNotification(playerId,
                                "Please finish the race and class steps before skipping the rest of the tutorial.");
                        return true;
                }
                playerCommandService.setTutorialStep(playerId, TutorialStep.DONE);
                OrientedLocation spawn = spawnRepository.getSpawn(player.getTeam());
                Direction direction = directionUtils.toDirection(spawn.getYaw());
                entityCommandService.teleport(playerId, spawn, direction);
                return true;
        }
        return false;
    }
}
