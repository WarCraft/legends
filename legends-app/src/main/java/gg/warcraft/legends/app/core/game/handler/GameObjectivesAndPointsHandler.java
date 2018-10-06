package gg.warcraft.legends.app.core.game.handler;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.legends.api.core.game.Game;
import gg.warcraft.legends.api.core.game.service.GameRepository;
import gg.warcraft.monolith.api.entity.team.Team;
import gg.warcraft.siege.api.objective.Objective;
import gg.warcraft.siege.api.objective.event.ObjectiveCapturedEvent;
import gg.warcraft.siege.api.objective.event.ObjectiveNeutralizedEvent;

import java.util.Map;

public class GameObjectivesAndPointsHandler {
    private final GameRepository gameRepository;

    @Inject
    public GameObjectivesAndPointsHandler(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Subscribe
    public void onObjectiveCapturedEvent(ObjectiveCapturedEvent event) {
        Game activeGame = gameRepository.getActiveGame();
        Objective objective = event.getObjective();
        Team newOccupiers = objective.getOccupiers();

        Map<String, Team> newObjectives = activeGame.getObjectives();
        newObjectives.put(objective.getId(), newOccupiers);

        Map<Team, Integer> newPoints = activeGame.getPoints();
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
        Game activeGame = gameRepository.getActiveGame();
        Objective objective = event.getObjective();
        Team newOccupiers = objective.getOccupiers();

        Map<String, Team> newObjectives = activeGame.getObjectives();
        newObjectives.put(objective.getId(), newOccupiers);

        Game newGame = activeGame.getCopyer()
                .withObjectives(newObjectives)
                .copy();

        gameRepository.save(newGame);
    }
}
