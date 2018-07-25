package gg.warcraft.legends.app.entity.player.rank;

import gg.warcraft.legends.api.entity.player.rank.Rank;
import gg.warcraft.legends.api.entity.player.rank.RankPrefix;
import gg.warcraft.legends.api.entity.player.rank.RankSuffix;
import gg.warcraft.monolith.api.util.ColorCode;

public class SimpleRank implements Rank {
    private final int level;
    private final int prestige;
    private final RankPrefix prefix;
    private final RankSuffix suffix;
    private final String prestigeChar;

    public SimpleRank(int level, int prestige, RankPrefix prefix, RankSuffix suffix, String prestigeChar) {
        this.level = level;
        this.prestige = prestige;
        this.prefix = prefix;
        this.suffix = suffix;
        this.prestigeChar = prestigeChar;
    }

    @Override
    public String getName() {
        String prestigePrefix = new String(new char[prestige]).replace("\0", prestigeChar);
        return prestigePrefix + " " + prefix.getName() + " " + suffix.getName();
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public ColorCode getColor() {
        return null;
    }
}
