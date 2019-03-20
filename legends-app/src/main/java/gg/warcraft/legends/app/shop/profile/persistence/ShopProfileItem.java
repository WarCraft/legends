package gg.warcraft.legends.app.shop.profile.persistence;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

public class ShopProfileItem {
    private final UUID playerId;
    private final boolean grandfathered;
    private final Set<String> decorations;
    private final Set<String> miniPets;
    private final Set<String> promotions;
    private final Set<String> specializations;

    @JsonCreator
    public ShopProfileItem(@JsonProperty("playerId") UUID playerId,
                           @JsonProperty("grandfathered") boolean grandfathered,
                           @JsonProperty("decorations") Set<String> decorations,
                           @JsonProperty("miniPets") Set<String> miniPets,
                           @JsonProperty("promotions") Set<String> promotions,
                           @JsonProperty("specializations") Set<String> specializations) {
        this.playerId = playerId;
        this.grandfathered = grandfathered;
        this.decorations = decorations != null ? decorations : Collections.emptySet();
        this.miniPets = miniPets != null ? miniPets : Collections.emptySet();
        this.promotions = promotions != null ? promotions : Collections.emptySet();
        this.specializations = specializations != null ? specializations : Collections.emptySet();
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public boolean isGrandfathered() {
        return grandfathered;
    }

    public Set<String> getDecorations() {
        return decorations;
    }

    public Set<String> getMiniPets() {
        return miniPets;
    }

    public Set<String> getPromotions() {
        return promotions;
    }

    public Set<String> getSpecializations() {
        return specializations;
    }
}
