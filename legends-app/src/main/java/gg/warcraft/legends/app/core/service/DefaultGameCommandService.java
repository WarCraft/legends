package gg.warcraft.legends.app.core.service;

import com.google.inject.Inject;
import gg.warcraft.legends.api.core.Game;
import gg.warcraft.legends.api.core.Skirmish;
import gg.warcraft.legends.api.core.War;
import gg.warcraft.legends.api.core.service.GameCommandService;
import gg.warcraft.legends.api.core.service.GameRepository;
import gg.warcraft.legends.app.core.SimpleSkirmish;
import gg.warcraft.legends.app.core.SimpleWar;
import gg.warcraft.monolith.api.util.Duration;
import gg.warcraft.monolith.api.util.TimeUtils;
import gg.warcraft.siege.api.objective.service.ObjectiveCommandService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.TreeMap;

public class DefaultGameCommandService implements GameCommandService {
    private final GameRepository gameRepository;
    private final ObjectiveCommandService objectiveCommandService;
    private final TimeUtils timeUtils;

    @Inject
    public DefaultGameCommandService(GameRepository gameRepository,
                                     ObjectiveCommandService objectiveCommandService,
                                     TimeUtils timeUtils) {
        this.gameRepository = gameRepository;
        this.objectiveCommandService = objectiveCommandService;
        this.timeUtils = timeUtils;
    }

    @Override
    public void initializeCurrentWar() {
        Game currentWar = gameRepository.getCurrentWar();
        if (currentWar == null) {
            throw new IllegalStateException("Failed to load current war, no current war seems to be present.");
        }

        currentWar.getObjectives().forEach((objectiveId, occupiers) -> {
            if (occupiers != null) {
                objectiveCommandService.setOccupiers(objectiveId, occupiers);
            }
        });
    }

    int getNextGameId() {
        Game currentWar = gameRepository.getCurrentWar();
        int newGameId = currentWar.getId() + 1;
        while (gameRepository.getGame(newGameId) != null) {
            newGameId += 1;
        }
        return newGameId;
    }

    @Override
    public void startSkirmish() {
        int newSkirmishId = getNextGameId();
        Duration timer = timeUtils.createDurationInSeconds(1800);
        String startTime = LocalDateTime.now().toString();
        Skirmish newSkirmish = new SimpleSkirmish(newSkirmishId, new HashMap<>(), new HashMap<>(), timer, startTime,
                null);
        gameRepository.save(newSkirmish);
        gameRepository.saveActiveGame(newSkirmishId);
    }

    @Override
    public void startNewWar() {
        int newWarId = getNextGameId();
        Duration timer = timeUtils.createDurationInSeconds(900);
        String startTime = LocalDateTime.now().toString();
        War newWar = new SimpleWar(newWarId, new HashMap<>(), new HashMap<>(), new TreeMap<>(), new HashMap<>(),
                timer, startTime, null);
        gameRepository.save(newWar);
        gameRepository.saveActiveGame(newWarId);
        gameRepository.saveCurrentWar(newWarId);
    }
}
