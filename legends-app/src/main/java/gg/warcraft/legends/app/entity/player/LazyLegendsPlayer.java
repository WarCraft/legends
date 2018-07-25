package gg.warcraft.legends.app.entity.player;

import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.LegendsPlayerProfile;
import gg.warcraft.monolith.api.entity.attribute.Attributes;
import gg.warcraft.monolith.api.entity.player.PlayerServerData;
import gg.warcraft.monolith.api.entity.status.Status;
import gg.warcraft.monolith.app.entity.player.LazyPlayer;

import java.util.function.Supplier;

public class LazyLegendsPlayer extends LazyPlayer implements LegendsPlayer {

    public LazyLegendsPlayer(Supplier<LegendsPlayerProfile> profileSupplier,
                             Supplier<PlayerServerData> serverDataSupplier,
                             Supplier<Attributes> attributesSupplier,
                             Supplier<Status> statusSupplier) {
        super(profileSupplier, serverDataSupplier, attributesSupplier, statusSupplier);
    }

    @Override
    public int getGoldCoins() {
        return getCurrency("Gold");
    }

    @Override
    public int getTotalGoldCoins() {
        return getCurrencyTotal("Gold");
    }

    @Override
    public int getFavor() {
        return getCurrency("Favor");
    }

    @Override
    public int getTotalFavor() {
        return getCurrencyTotal("Favor");
    }
}
