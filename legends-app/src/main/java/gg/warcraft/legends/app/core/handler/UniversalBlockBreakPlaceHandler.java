package gg.warcraft.legends.app.core.handler;

import com.google.common.eventbus.Subscribe;
import gg.warcraft.monolith.api.world.block.event.BlockPreBreakEvent;
import gg.warcraft.monolith.api.world.block.event.BlockPrePlaceEvent;

public class UniversalBlockBreakPlaceHandler {

    @Subscribe
    public void onBlockPreBreakEvent(BlockPreBreakEvent event) {
        event.cancel();
    }

    @Subscribe
    public void onBlockPrePlaceEvent(BlockPrePlaceEvent event) {
        event.cancel();
    }
}
