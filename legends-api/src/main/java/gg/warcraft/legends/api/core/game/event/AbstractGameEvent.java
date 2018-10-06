package gg.warcraft.legends.api.core.game.event;

public abstract class AbstractGameEvent implements GameEvent {
    private final int gameId;

    public AbstractGameEvent(int gameId) {
        this.gameId = gameId;
    }

    @Override
    public int getGameId() {
        return gameId;
    }
}
