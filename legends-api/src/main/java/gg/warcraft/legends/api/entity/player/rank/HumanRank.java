package gg.warcraft.legends.api.entity.player.rank;

public enum HumanRank implements RankSuffix {
    SQUIRE("Squire"),
    FOOTMAN("Footman"),
    KNIGHT("Knight"),
    CAPTAIN("Captain"),
    CHAMPION("Chamption"),
    HERO("Hero"),
    LEGEND("Legend");

    private final String name;

    HumanRank(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
