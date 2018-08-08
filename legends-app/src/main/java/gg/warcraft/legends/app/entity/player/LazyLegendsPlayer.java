package gg.warcraft.legends.app.entity.player;

import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.rank.Rank;
import gg.warcraft.monolith.api.entity.attribute.Attributes;
import gg.warcraft.monolith.api.entity.player.PlayerProfile;
import gg.warcraft.monolith.api.entity.player.PlayerServerData;
import gg.warcraft.monolith.api.entity.status.Status;
import gg.warcraft.monolith.app.entity.player.LazyPlayer;

import java.util.function.Supplier;

public class LazyLegendsPlayer extends LazyPlayer implements LegendsPlayer {

    public LazyLegendsPlayer(Supplier<PlayerProfile> profileSupplier,
                             Supplier<PlayerServerData> serverDataSupplier,
                             Supplier<Attributes> attributesSupplier,
                             Supplier<Status> statusSupplier) {
        super(profileSupplier, serverDataSupplier, attributesSupplier, statusSupplier);
    }

    @Override
    public int getGold() {
        return getCurrency(LegendsCurrency.GOLD.getName());
    }

    @Override
    public int getLifetimeGold() {
        return getLifetimeCurrency(LegendsCurrency.GOLD.getName());
    }

    @Override
    public int getFavor() {
        return getCurrency(LegendsCurrency.FAVOR.getName());
    }

    @Override
    public int getLifetimeFavor() {
        return getLifetimeCurrency(LegendsCurrency.FAVOR.getName());
    }

    @Override
    public Rank getRank() {
        throw new IllegalStateException("Not implemented");
    }
}
