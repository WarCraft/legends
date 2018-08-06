package gg.warcraft.legends.app.combat.ranger.marksman;

import com.google.inject.Singleton;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Singleton
public class FocusRepository {
    private final Map<UUID, Focus> focussedPlayers;

    public FocusRepository() {
        this.focussedPlayers = new HashMap<>();
    }

    public Focus getFocus(UUID playerId) {
        return focussedPlayers.get(playerId);
    }

    public void save(Focus focus) {
        focussedPlayers.put(focus.getCasterId(), focus);
    }

    public void delete(Focus focus) {
        focussedPlayers.remove(focus.getCasterId());
    }
}
