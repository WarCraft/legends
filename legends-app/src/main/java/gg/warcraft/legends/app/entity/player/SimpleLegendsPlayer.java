package gg.warcraft.legends.app.entity.player;

import gg.warcraft.legends.api.entity.player.LegendsPlayer;
import gg.warcraft.legends.api.entity.player.rank.Rank;
import gg.warcraft.monolith.api.entity.player.Player;
import gg.warcraft.monolith.api.entity.player.PlayerDecorator;

public class SimpleLegendsPlayer implements LegendsPlayer, PlayerDecorator {
    private final Player player;

    public SimpleLegendsPlayer(Player player) {
        this.player = player;
    }

    @Override
    public Player getPlayer() {
        return player;
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
