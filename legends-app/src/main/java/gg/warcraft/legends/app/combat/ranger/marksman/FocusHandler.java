package gg.warcraft.legends.app.combat.ranger.marksman;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import gg.warcraft.monolith.api.entity.player.event.PlayerDisconnectEvent;

public class FocusHandler {
    private final FocusRepository focusRepository;

    @Inject
    public FocusHandler(FocusRepository focusRepository) {
        this.focusRepository = focusRepository;
    }

    @Subscribe
    public void onPlayerDisconnectEvent(PlayerDisconnectEvent event) {
        Focus focus = focusRepository.getFocus(event.getPlayerId());
        if (focus != null) {
            focus.cancel();
        }
    }

    /* TODO
    @Subscribe
    public void onPlayerSpecializationChangedEvent(PlayerSpecializationChangedEvent event) {

    }
    */
}
