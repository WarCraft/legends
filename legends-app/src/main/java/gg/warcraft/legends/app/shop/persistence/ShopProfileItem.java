package gg.warcraft.legends.app.shop.persistence;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public class ShopProfileItem {
    private final boolean grandfathered;
    private final Set<Integer> decorations;
    private final Set<Integer> miniPets;
    private final Set<Integer> promotions;

    @JsonCreator
    public ShopProfileItem(@JsonProperty("grandfathered") boolean grandfathered,
                           @JsonProperty("decorations") Set<Integer> decorations,
                           @JsonProperty("miniPets") Set<Integer> miniPets,
                           @JsonProperty("promotions") Set<Integer> promotions) {
        this.grandfathered = grandfathered;
        this.decorations = decorations;
        this.miniPets = miniPets;
        this.promotions = promotions;
    }

    public boolean isGrandfathered() {
        return grandfathered;
    }

    public Set<Integer> getDecorations() {
        return decorations;
    }

    public Set<Integer> getMiniPets() {
        return miniPets;
    }

    public Set<Integer> getPromotions() {
        return promotions;
    }
}
