package gg.warcraft.legends.api.entity.player.rank;

public enum RankPrefix {
    WOOD("Wood"),
    STONE("Stone"),
    IRON("Iron"),
    GOLD("Gold"),
    EMERALD("Emerald"),
    DIAMOND("Diamond"),
    OBSIDIAN("Obsidian");

    private final String name;

    RankPrefix(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
