package gg.warcraft.legends.app.core.game.handler;

import com.google.inject.Inject;
import gg.warcraft.legends.api.core.game.Game;
import gg.warcraft.legends.api.core.game.GameType;
import gg.warcraft.legends.api.core.game.service.GameRepository;
import gg.warcraft.monolith.api.util.Duration;
import gg.warcraft.monolith.api.util.TimeUtils;

public class GameTimerHandler implements Runnable {
    private final GameRepository gameRepository;
    private final TimeUtils timeUtils;

    @Inject
    public GameTimerHandler(GameRepository gameRepository, TimeUtils timeUtils) {
        this.gameRepository = gameRepository;
        this.timeUtils = timeUtils;
    }

    @Override
    public void run() {
        Game activeGame = gameRepository.getActiveGame();
        int timerInSeconds = activeGame.getTimer().inSeconds();
        if (timerInSeconds <= 1) {
            GameType gameType = activeGame.getType();
            switch (gameType) {
                case WAR:
                    // TODO run tally
                    break;
                case SKIRMISH:
                    // TODO end skirmish
                    break;
                default:
                    throw new IllegalStateException("Failed to handle timer for game with unrecognized type " + gameType);
            }
        } else {
            Duration newTimer = timeUtils.createDurationInSeconds(timerInSeconds - 1);
            Game newGame = activeGame.getCopyer()
                    .withTimer(newTimer)
                    .copy();
            gameRepository.save(newGame);
        }
    }
}
