package gg.warcraft.legends.api.shop;

public enum DecorationPackId {
    BASIC("BASIC");

    private final String id;

    DecorationPackId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id;
    }
}
