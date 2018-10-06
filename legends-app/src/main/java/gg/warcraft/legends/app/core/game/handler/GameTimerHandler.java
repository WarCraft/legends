package gg.warcraft.legends.app.core.game.handler;

import com.google.inject.Inject;
import gg.warcraft.legends.api.core.game.service.GameCommandService;

public class GameTimerHandler implements Runnable {
    private final GameCommandService gameCommandService;

    @Inject
    public GameTimerHandler(GameCommandService gameCommandService) {
        this.gameCommandService = gameCommandService;
    }

    @Override
    public void run() {
        gameCommandService.tickActiveGame();
    }
}
