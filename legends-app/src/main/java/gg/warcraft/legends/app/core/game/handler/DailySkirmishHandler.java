package gg.warcraft.legends.app.core.game.handler;

import com.google.inject.Inject;
import gg.warcraft.legends.api.core.game.Game;
import gg.warcraft.legends.api.core.game.GameType;
import gg.warcraft.legends.api.core.game.service.GameCommandService;
import gg.warcraft.legends.api.core.game.service.GameQueryService;

import java.time.LocalTime;

public class DailySkirmishHandler implements Runnable {
    private static final LocalTime FIRST_DAILY_SKIRMISH_TIME = LocalTime.of(15, 0);
    private static final LocalTime SECOND_DAILY_SKIRMISH_TIME = LocalTime.of(18, 0);
    private static final LocalTime THIRD_DAILY_SKIRMISH_TIME = LocalTime.of(21, 0);

    private final GameQueryService gameQueryService;
    private final GameCommandService gameCommandService;

    @Inject
    public DailySkirmishHandler(GameQueryService gameQueryService,
                                GameCommandService gameCommandService) {
        this.gameQueryService = gameQueryService;
        this.gameCommandService = gameCommandService;
    }

    private boolean isTimeForSkirmish(LocalTime time) {
        return time.equals(FIRST_DAILY_SKIRMISH_TIME) ||
                time.equals(SECOND_DAILY_SKIRMISH_TIME) ||
                time.equals(THIRD_DAILY_SKIRMISH_TIME);
    }

    @Override
    public void run() {
        LocalTime thisMinute = LocalTime.now().withSecond(0).withNano(0);
        if (!isTimeForSkirmish(thisMinute)) {
            return;
        }

        Game activeGame = gameQueryService.getActiveGame();
        if (activeGame.getType() == GameType.SKIRMISH) {
            return;
        }

        gameCommandService.startSkirmish();
    }
}
