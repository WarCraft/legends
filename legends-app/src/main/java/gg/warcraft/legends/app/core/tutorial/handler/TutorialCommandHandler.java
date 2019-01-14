package gg.warcraft.legends.app.core.tutorial.handler;

import com.google.inject.Inject;
import gg.warcraft.combat.api.specialization.service.SpecializationCommandService;
import gg.warcraft.legends.api.core.tutorial.TutorialStep;
import gg.warcraft.legends.api.entity.LegendsTeam;
import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.service.LegendsPlayerQueryService;
import gg.warcraft.monolith.api.command.Command;
import gg.warcraft.monolith.api.command.CommandHandler;
import gg.warcraft.monolith.api.command.CommandSender;
import gg.warcraft.monolith.api.entity.team.service.TeamCommandService;

import java.util.List;
import java.util.UUID;

public class TutorialCommandHandler implements CommandHandler {
    private final LegendsPlayerQueryService playerQueryService;
    private final TeamCommandService teamCommandService;
    private final SpecializationCommandService specializationCommandService;

    @Inject
    public TutorialCommandHandler(LegendsPlayerQueryService playerQueryService, TeamCommandService teamCommandService,
                                  SpecializationCommandService specializationCommandService) {
        this.playerQueryService = playerQueryService;
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
        if (player.getTutorialStep() == TutorialStep.DONE) {
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
            default:
                return false;
        }
    }
}
