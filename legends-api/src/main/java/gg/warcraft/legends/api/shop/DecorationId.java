package gg.warcraft.legends.api.shop;

public enum DecorationId {
    ;

    private final String id;

    DecorationId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id;
    }
}
