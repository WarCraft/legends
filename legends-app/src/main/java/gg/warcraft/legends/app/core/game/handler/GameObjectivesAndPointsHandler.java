package gg.warcraft.legends.app.core.game.handler;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.legends.api.core.game.Game;
import gg.warcraft.legends.api.core.game.GameType;
import gg.warcraft.legends.api.core.game.service.GameRepository;
import gg.warcraft.legends.api.entity.LegendsTeam;
import gg.warcraft.monolith.api.entity.EntityType;
import gg.warcraft.monolith.api.entity.event.EntityFatalDamageEvent;
import gg.warcraft.monolith.api.entity.player.Player;
import gg.warcraft.monolith.api.entity.player.service.PlayerQueryService;
import gg.warcraft.siege.api.objective.Objective;
import gg.warcraft.siege.api.objective.event.ObjectiveCapturedEvent;
import gg.warcraft.siege.api.objective.event.ObjectiveNeutralizedEvent;

import java.util.Map;
import java.util.UUID;

public class GameObjectivesAndPointsHandler {
    private final PlayerQueryService playerQueryService;
    private final GameRepository gameRepository;

    @Inject
    public GameObjectivesAndPointsHandler(PlayerQueryService playerQueryService, GameRepository gameRepository) {
        this.playerQueryService = playerQueryService;
        this.gameRepository = gameRepository;
    }

    @Subscribe
    public void onObjectiveCapturedEvent(ObjectiveCapturedEvent event) {
        Objective objective = event.getObjective();
        if (objective.getType().equals("TUTORIAL")) {
            return;
        }

        Game activeGame = gameRepository.getActiveGame();
        LegendsTeam newOccupiers = (LegendsTeam) objective.getOccupiers();
        Map<String, LegendsTeam> newObjectives = activeGame.getObjectives();
        newObjectives.put(objective.getId(), newOccupiers);

        Map<LegendsTeam, Integer> newPoints = activeGame.getPoints();
        int currentTeamPoints = newPoints.getOrDefault(newOccupiers, 0);
        int capturePoints = gameRepository.getCapturePoints(objective.getType());
        int newTeamPoints = currentTeamPoints + capturePoints;
        newPoints.put(newOccupiers, newTeamPoints);

        Game newGame = activeGame.getCopyer()
                .withObjectives(newObjectives)
                .withPoints(newPoints)
                .copy();
        gameRepository.save(newGame);
    }

    @Subscribe
    public void onObjectiveNeutralizedEvent(ObjectiveNeutralizedEvent event) {
        Objective objective = event.getObjective();
        if (objective.getType().equals("TUTORIAL")) {
            return;
        }

        Game activeGame = gameRepository.getActiveGame();
        LegendsTeam newOccupiers = (LegendsTeam) objective.getOccupiers();
        Map<String, LegendsTeam> newObjectives = activeGame.getObjectives();
        newObjectives.put(objective.getId(), newOccupiers);

        Game newGame = activeGame.getCopyer()
                .withObjectives(newObjectives)
                .copy();
        gameRepository.save(newGame);
    }

    @Subscribe
    public void onEntityFatalDamageEvent(EntityFatalDamageEvent event) {
        if (event.getEntityType() != EntityType.PLAYER) {
            return;
        }

        Player player = playerQueryService.getPlayer(event.getEntityId());
        if (player != null) {
            UUID killerId = event.getDamage().getSource().getEntityId();
            if (killerId != null) {
                Player killer = playerQueryService.getPlayer(killerId);
                if (killer != null) {
                    LegendsTeam killerTeam = (LegendsTeam) killer.getTeam();
                    if (killerTeam != null) {
                        Game activeGame = gameRepository.getActiveGame();
                        Map<LegendsTeam, Integer> newPoints = activeGame.getPoints();
                        int currentTeamPoints = newPoints.getOrDefault(killerTeam, 0);
                        int newTeamPoints = activeGame.getType() == GameType.SKIRMISH
                                ? currentTeamPoints + 3
                                : currentTeamPoints + 1;
                        newPoints.put(killerTeam, newTeamPoints);

                        Game newGame = activeGame.getCopyer()
                                .withPoints(newPoints)
                                .copy();
                        gameRepository.save(newGame);
                    }
                }
            }
        }
    }
}
