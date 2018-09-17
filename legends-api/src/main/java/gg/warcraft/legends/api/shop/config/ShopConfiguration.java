package gg.warcraft.legends.api.shop.config;

import java.util.List;

public interface ShopConfiguration {

    List<DecorationConfiguration> getDecorations();

    List<DecorationPackConfiguration> getDecorationPacks();

    List<MiniPetConfiguration> getMiniPets();

    List<PromotionConfiguration> getPromotions();
}
