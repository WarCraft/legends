package gg.warcraft.legends.api.shop;

public enum PromotionId {
    FOUR_YEAR_OLD_CAKE("FOUR_YEAR_OLD_CAKE");

    private final String id;

    PromotionId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id;
    }
}
