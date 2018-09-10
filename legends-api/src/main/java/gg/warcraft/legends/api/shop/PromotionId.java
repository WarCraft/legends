package gg.warcraft.legends.api.shop;

public enum PromotionId {
//    FOUR_YEAR_OLD_CAKE(1, ItemType.CAKE, "Four Year Old Cake", ItemRarity.RARE,
//            "It doesn't look that tasty after all this time,",
//            "but its still worth celebrating! Right..?");
    ;
    private final String id;

    PromotionId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id;
    }
}
