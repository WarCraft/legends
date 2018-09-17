package gg.warcraft.legends.app.shop.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import gg.warcraft.legends.api.shop.config.DecorationConfiguration;
import gg.warcraft.legends.api.shop.config.DecorationPackConfiguration;
import gg.warcraft.legends.api.shop.config.MiniPetConfiguration;
import gg.warcraft.legends.api.shop.config.PromotionConfiguration;
import gg.warcraft.legends.api.shop.config.ShopConfiguration;

import java.util.ArrayList;
import java.util.List;

public class JacksonShopConfiguration implements ShopConfiguration {
    private final List<DecorationConfiguration> decorations;
    private final List<DecorationPackConfiguration> decorationPacks;
    private final List<MiniPetConfiguration> miniPets;
    private final List<PromotionConfiguration> promotions;

    @JsonCreator
    public JacksonShopConfiguration(@JsonProperty("decorations") List<DecorationConfiguration> decorations,
                                    @JsonProperty("decorationPacks") List<DecorationPackConfiguration> decorationPacks,
                                    @JsonProperty("miniPets") List<MiniPetConfiguration> miniPets,
                                    @JsonProperty("promotions") List<PromotionConfiguration> promotions) {
        this.decorations = decorations;
        this.decorationPacks = decorationPacks;
        this.miniPets = miniPets;
        this.promotions = promotions;
    }

    @Override
    public List<DecorationConfiguration> getDecorations() {
        return new ArrayList<>(decorations);
    }

    @Override
    public List<DecorationPackConfiguration> getDecorationPacks() {
        return new ArrayList<>(decorationPacks);
    }

    @Override
    public List<MiniPetConfiguration> getMiniPets() {
        return new ArrayList<>(miniPets);
    }

    @Override
    public List<PromotionConfiguration> getPromotions() {
        return new ArrayList<>(promotions);
    }
}
