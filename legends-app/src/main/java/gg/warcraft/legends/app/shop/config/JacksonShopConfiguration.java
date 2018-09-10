package gg.warcraft.legends.app.shop.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import gg.warcraft.legends.api.shop.config.DecorationConfiguration;
import gg.warcraft.legends.api.shop.config.DecorationPackConfiguration;
import gg.warcraft.legends.api.shop.config.MiniPetConfiguration;
import gg.warcraft.legends.api.shop.config.ShopConfiguration;

import java.util.List;

public class JacksonShopConfiguration implements ShopConfiguration {
    private final List<DecorationConfiguration> decorations;
    private final List<DecorationPackConfiguration> decorationPacks;
    private final List<MiniPetConfiguration> miniPets;

    @JsonCreator
    public JacksonShopConfiguration(@JsonProperty("decorations") List<DecorationConfiguration> decorations,
                                    @JsonProperty("decorationPacks") List<DecorationPackConfiguration> decorationPacks,
                                    @JsonProperty("miniPets") List<MiniPetConfiguration> miniPets) {
        this.decorations = decorations;
        this.decorationPacks = decorationPacks;
        this.miniPets = miniPets;
    }

    @Override
    public List<DecorationConfiguration> getDecorations() {
        return decorations;
    }

    @Override
    public List<DecorationPackConfiguration> getDecorationPacks() {
        return decorationPacks;
    }

    @Override
    public List<MiniPetConfiguration> getMiniPets() {
        return miniPets;
    }
}
