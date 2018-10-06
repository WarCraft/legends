package gg.warcraft.legends.app.config;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import com.fasterxml.jackson.databind.module.SimpleModule;
import gg.warcraft.legends.api.config.RankPrefixConfiguration;
import gg.warcraft.legends.api.config.ServerConfiguration;
import gg.warcraft.legends.api.item.crate.config.LootCrateConfiguration;
import gg.warcraft.legends.api.item.crate.config.LootCrateDropTableConfiguration;
import gg.warcraft.legends.api.shop.config.DecorationConfiguration;
import gg.warcraft.legends.api.shop.config.DecorationPackConfiguration;
import gg.warcraft.legends.api.shop.config.MiniPetConfiguration;
import gg.warcraft.legends.api.shop.config.PromotionConfiguration;
import gg.warcraft.legends.api.shop.config.ShopConfiguration;
import gg.warcraft.legends.api.shop.config.ShopItemConfiguration;
import gg.warcraft.legends.app.item.crate.config.JacksonLootCrateConfiguration;
import gg.warcraft.legends.app.item.crate.config.JacksonLootCrateDropTableConfiguration;
import gg.warcraft.legends.app.shop.config.JacksonDecorationConfiguration;
import gg.warcraft.legends.app.shop.config.JacksonDecorationPackConfiguration;
import gg.warcraft.legends.app.shop.config.JacksonMiniPetConfiguration;
import gg.warcraft.legends.app.shop.config.JacksonPromotionConfiguration;
import gg.warcraft.legends.app.shop.config.JacksonShopConfiguration;
import gg.warcraft.legends.app.shop.config.JacksonShopItemConfiguration;

public class LegendsMapperModule extends SimpleModule {

    public LegendsMapperModule() {
        super("LegendsMapperModule", Version.unknownVersion());
        SimpleAbstractTypeResolver resolver = new SimpleAbstractTypeResolver();

        // Server configuration
        resolver.addMapping(ServerConfiguration.class, JacksonServerConfiguration.class);
        resolver.addMapping(RankPrefixConfiguration.class, JacksonRankPrefixConfiguration.class);

        // Shop configuration
        resolver.addMapping(ShopConfiguration.class, JacksonShopConfiguration.class);
        resolver.addMapping(ShopItemConfiguration.class, JacksonShopItemConfiguration.class);
        resolver.addMapping(DecorationConfiguration.class, JacksonDecorationConfiguration.class);
        resolver.addMapping(DecorationPackConfiguration.class, JacksonDecorationPackConfiguration.class);
        resolver.addMapping(MiniPetConfiguration.class, JacksonMiniPetConfiguration.class);
        resolver.addMapping(PromotionConfiguration.class, JacksonPromotionConfiguration.class);

        // Loot crate configuration
        resolver.addMapping(LootCrateConfiguration.class, JacksonLootCrateConfiguration.class);
        resolver.addMapping(LootCrateDropTableConfiguration.class, JacksonLootCrateDropTableConfiguration.class);

        setAbstractTypes(resolver);
    }
}
