package gg.warcraft.legends.app.core.tutorial.handler;

import com.google.inject.Inject;
import gg.warcraft.combat.api.specialization.service.SpecializationCommandService;
import gg.warcraft.legends.api.core.tutorial.TutorialStep;
import gg.warcraft.legends.api.entity.LegendsTeam;
import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerCommandService;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.monolith.api.command.Command;
import gg.warcraft.monolith.api.command.CommandHandler;
import gg.warcraft.monolith.api.command.CommandSender;
import gg.warcraft.monolith.api.entity.team.service.TeamCommandService;

import java.util.List;
import java.util.UUID;

public class TutorialCommandHandler implements CommandHandler {
    private final LegendsPlayerQueryService playerQueryService;
    private final LegendsPlayerCommandService playerCommandService;
    private final TeamCommandService teamCommandService;
    private final SpecializationCommandService specializationCommandService;

    @Inject
    public TutorialCommandHandler(LegendsPlayerQueryService playerQueryService,
                                  LegendsPlayerCommandService playerCommandService,
                                  TeamCommandService teamCommandService,
                                  SpecializationCommandService specializationCommandService) {
        this.playerQueryService = playerQueryService;
        this.playerCommandService = playerCommandService;
        this.teamCommandService = teamCommandService;
        this.specializationCommandService = specializationCommandService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, List<String> arguments) {
        if (!sender.isPlayer() || arguments.isEmpty()) {
            return false;
        }

        UUID playerId = sender.getPlayerId();
        LegendsPlayer player = playerQueryService.getPlayer(playerId);
        TutorialStep currentStep = player.getTutorialStep();
        if (currentStep == TutorialStep.DONE) {
            return false;
        }

        String argument = arguments.get(0);
        switch (label) {
            case "setracetutorial":
                LegendsTeam race = LegendsTeam.valueOf(argument);
                teamCommandService.setTeam(race, playerId);
                return true;
            case "setclasstutorial":
                specializationCommandService.setSpecialization(argument, playerId);
                return true;
            case "skiptutorial":
                if (currentStep == TutorialStep.START
                        || currentStep == TutorialStep.RACE
                        || currentStep == TutorialStep.CLASS) {
                    playerCommandService.sendNotification(playerId,
                            "Please finish the race and class steps before skipping the rest of the tutorial.");
                }
                playerCommandService.setTutorialStep(playerId, TutorialStep.DONE);
                // TODO TP to race specific capital
                return true;
            default:
                return false;
        }
    }
}
